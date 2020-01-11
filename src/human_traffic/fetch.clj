(ns human-traffic.fetch
  (:gen-class
   :methods [^:static [handler [Object] Object]])
  (:require [cheshire.core :as che]
            [clj-http.client :as client]
            [cuerdas.core :as cuerdas]
            [environ.core :refer [env]]
            [failjure.core :as f]
            [human-traffic.common :refer [conn sg-api-token]]
            [java-time :as t]
            [schema.core :as s]
            [sendgrid.core :as sg]
            [somnium.congomongo :as m]))


(def libraries ["https://webapi.affluences.com/api/fillRate?token=UBK1jbZGtvLFO1&uuid=0a29d432-8814-4ac3-9010-b4958904e814" ;NB
                "https://webapi.affluences.com/api/fillRate?token=LZdnFCQiXCzLAy&uuid=602bd735-dff2-4320-b95a-fa3280afafc3" ;AX
                "https://webapi.affluences.com/api/fillRate?token=mPNtw1Dw95fOKp&uuid=fc5c809b-dc18-4d22-a607-b22fb565f4fd" ;Archi
                "https://webapi.affluences.com/api/fillRate?token=ryjtReSZqQPoga&uuid=1924f2ff-32e8-4c27-b1bd-8698b5c4c434" ;Droit
                "https://webapi.affluences.com/api/fillRate?token=PaOUBXhjjfExQl&uuid=cdd5317f-97fd-4bb4-a20b-d54f2ef83bd3" ;BSS
                "https://webapi.affluences.com/api/fillRate?token=XxkgcXx7krYCHg&uuid=89ff5744-08bd-4479-9cb5-04dbf00b234f"]) ;BST


(def data-shape
  "A schema for the Affluences API"
  {:site_id s/Int
   :site_name s/Str
   :progress s/Int
   :previsions {:value []
                :localized_forecasts s/Str}
   :critical_message (s/maybe s/Str)
   :current_state {:state s/Str
                   :value s/Int
                   :localized_state s/Str
                   :localized_closed s/Str}
   :localized_strings {:info s/Str}})


(defn fetch-api
  "Calls the external resource and returns the result. If an Exception
  occurs, Failjure lib catches it and treats it as a failure"
  [url]
  (f/try* (:body (client/get url {:as :json}))))


(defn validate-data
  "Makes sure Affluences have not changed their API"
  [data]
  (try
    (s/validate data-shape data)
    (catch Exception e
      (f/fail "Data is not valid ! Schema returned error: %s" e))))


(defn not-ok
  "Send a notification email, prints the error and crash the process"
  [error]
  (sg/send-email {:api-token sg-api-token
                  :from "notification@human-traffic.io"
                  :to (str (env :notification-recipient))
                  :subject "Human traffic error"
                  :message (f/message error)})
  (println (f/message error))
  (throw (Exception. "Error")))


(defn expand-data
  "Add an Instant field to the map"
  [lib]
  (merge {:inst (t/instant)} lib))


(defn save-data
  "Saves the data fetched from the API to Mongo. Lib is a map containing
  info about the library (times, state, name, etc"
  [lib]
  (m/with-mongo conn
    (m/insert! (keyword (cuerdas/slug (:site_name lib))) lib)))


(defn process
  "Puts the library through a series of transformations, ending in the
  saving of the data fetched. If there is an error at some point, the
  procedure shotcuts to the not-ok procedure (failjure feature)"
  [lib]
  (let [result (f/ok->> lib
                        (fetch-api)
                        (validate-data)
                        (expand-data)
                        (save-data))]
    (if (f/failed? result)
      (not-ok result)
      result)))


(def lambda_default
  "AWS Lambda stuff... It is the answer template for AWS API Gateway"
  {"isBase64Encoded" false
   "headers" {}
   "statusCode" 200})

(defn -handler
  "Fetches data from the API and stores the record in a database. Libraries
  are all the different URL endpoints from which to fetch data"
  [req]
  (doseq [lib libraries]
    (process lib))
  (merge lambda_default {"body" (che/generate-string "OK")}))
