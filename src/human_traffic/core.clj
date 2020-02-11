(ns human-traffic.core
  (:gen-class
   :methods [^:static [handler [Object] Object]])
  (:require [cheshire.core :as che]
            [cheshire.generate :refer [add-encoder encode-str]]
            [human-traffic.common :refer [conn]]
            [java-time :as t]
            [somnium.congomongo :as m])
  (:import org.bson.types.ObjectId))

(defn get-occupation
  "Builds and performs the SQL query according to the passed parameters:
  start, end and location"
  [start end location]
  (let [ts-start (t/instant start)
        ts-end (t/instant end)]
    (m/with-mongo conn
      (m/fetch (keyword location) :where {:inst {"$gt" ts-start "$lt" ts-end}}))))


;; ;;; Because this is on AWS Lambda + API Gateway, there is no need for
;; ;;; any HTTP servers or routing middleware. Just fetch from DB and
;; ;;; return JSON

;; ;;; AWS Lambda proxy response format
;; ;;; {
;; ;;;    "isBase64Encoded": true/false,
;; ;;;    "statusCode": 200,
;; ;;;    "headers": {},
;; ;;;    "body": "The body" ;; <-- stringified body !
;; ;;; }
(def lambda_default
  {"isBase64Encoded" false
   "headers" {}
   "statusCode" 200})

;;; This simple line allows Cheshire to encode the ObjectId type as a
;;; str.
(add-encoder ObjectId encode-str)

(defn -handler
  "API Gateway transforms query GET params into POST fields, which can
  be found in s.queryStringParameters"
  [s]
  (let [input (get s "queryStringParameters")
        start (get input "start")
        end (get input "end")
        location (get input "location")
        res (get-occupation start end location)]
    (merge lambda_default {"body" (che/generate-string res)})))
