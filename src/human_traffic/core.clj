(ns human-traffic.core
  (:gen-class
   :methods [^:static [handler [Object] Object]])
  (:require [cheshire.core :as che]))


;;; Because this is on AWS Lambda + API Gateway, there is no need for
;;; any HTTP servers or routing middleware. Just fetch from DB and
;;; return JSON

;;; AWS Lambda proxy response format
;;; {
;;;    "isBase64Encoded": true/false,
;;;    "statusCode": 200,
;;;    "headers": {},
;;;    "body": "The body" ;; <-- stringified body !
;;; }

(def lambda_default
  {"isBase64Encoded" false
   "headers" {}
   "statusCode" 200})


(defn -handler
  "API Gateway transforms query GET params into POST fields, which can
  be found in s"
  [s]
  (merge lambda_default {"body" (che/generate-string s)}))
