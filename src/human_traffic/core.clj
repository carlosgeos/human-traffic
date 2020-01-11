(ns human-traffic.core
  (:gen-class
   :methods [^:static [handler [Object] Object]])
  (:require [cheshire.core :as che]
            [human-traffic.common :as common]
            [java-time :as t]))

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

;; (def lambda_default
;;   {"isBase64Encoded" false
;;    "headers" {}
;;    "statusCode" 200})

;; (defn get-occupation
;;   "Builds and performs the SQL query according to the passed parameters:
;;   start, end and location"
;;   [start end & {:keys [location] :or {location "nb,ax,archi,droit,bss,bst"}}]
;;   (let [start (t/sql-timestamp (t/local-date-time "yyyy-MM-dd HH:mm" start))
;;         end (t/sql-timestamp (t/local-date-time "yyyy-MM-dd HH:mm" end))]
;;     (j/query common/db
;;              [(format "select ts, %s from people where ts >= '%s' and ts <= '%s'"
;;                       location start end)])))

;; (defn -handler
;;   "API Gateway transforms query GET params into POST fields, which can
;;   be found in s.queryStringParameters"
;;   [s]
;;   (let [input (get s "queryStringParameters")
;;         start (get input "start")
;;         end (get input "end")
;;         location (get input "location")
;;         res (get-occupation start end :location location)]
;;     (merge lambda_default {"body" (che/generate-string res)})))
