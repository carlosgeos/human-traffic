(ns human-traffic.fetch
  (:gen-class
   :methods [^:static [handler [String] String]])
  (:require [clj-http.client :as client]
            [clojure.java.jdbc :as j]
            [environ.core :refer [env]]
            [java-time :as t]))

(def database-url
  (env :database-url))

(def db-spec (str database-url "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"))

(def rooms {:nb "https://webapi.affluences.com/api/fillRate?token=UBK1jbZGtvLFO1&uuid=0a29d432-8814-4ac3-9010-b4958904e814"
            :ax "https://webapi.affluences.com/api/fillRate?token=LZdnFCQiXCzLAy&uuid=602bd735-dff2-4320-b95a-fa3280afafc3"
            :archi "https://webapi.affluences.com/api/fillRate?token=mPNtw1Dw95fOKp&uuid=fc5c809b-dc18-4d22-a607-b22fb565f4fd"
            :droit "https://webapi.affluences.com/api/fillRate?token=ryjtReSZqQPoga&uuid=1924f2ff-32e8-4c27-b1bd-8698b5c4c434"
            :bss "https://webapi.affluences.com/api/fillRate?token=PaOUBXhjjfExQl&uuid=cdd5317f-97fd-4bb4-a20b-d54f2ef83bd3"
            :bst "https://webapi.affluences.com/api/fillRate?token=XxkgcXx7krYCHg&uuid=89ff5744-08bd-4479-9cb5-04dbf00b234f"})


(defn url->percent
  "Function to be passed to map when mapping a map. Returns the
  percentage out of an URL endpoint"
  [[k v]]
  (vector k (:progress (:body (client/get v {:as :json})))))

(defn get-percents
  "Makes the HTTP requests to gather the percentage info on all the
  rooms"
  [rooms]
  (into {} (map url->percent rooms)))

(defn insert-record
  "Insert percentage info + timestamp in the database"
  [rooms]
  (j/insert! db-spec :people
             (merge {:ts (t/local-date-time)} (get-percents rooms))))

(defn -handler
  "Fetches data from the API and stores the record in a database. Rooms
  are all the different URL endpoints from which to fetch data"
  [s]
  (insert-record rooms))
