(ns human-traffic.fetch
  (:gen-class
   :methods [^:static [handler [String] String]])
  (:require [clj-http.client :as client]
            [clojure.java.jdbc :as j]
            [environ.core :refer [env]]
            ))

(def database-url
  (env :database-url))

(def db-spec (str database-url "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"))

(def nb "https://webapi.affluences.com/api/fillRate?token=LZdnFCQiXCzLAy&uuid=602bd735-dff2-4320-b95a-fa3280afafc3")
(def ax "https://webapi.affluences.com/api/fillRate?token=LZdnFCQiXCzLAy&uuid=602bd735-dff2-4320-b95a-fa3280afafc3")
(def archi "https://webapi.affluences.com/api/fillRate?token=mPNtw1Dw95fOKp&uuid=fc5c809b-dc18-4d22-a607-b22fb565f4fd")
(def droit "https://webapi.affluences.com/api/fillRate?token=ryjtReSZqQPoga&uuid=1924f2ff-32e8-4c27-b1bd-8698b5c4c434")
(def bss "https://webapi.affluences.com/api/fillRate?token=PaOUBXhjjfExQl&uuid=cdd5317f-97fd-4bb4-a20b-d54f2ef83bd3")
(def bst "https://webapi.affluences.com/api/fillRate?token=XxkgcXx7krYCHg&uuid=89ff5744-08bd-4479-9cb5-04dbf00b234f")

(:body (client/get nb {:as :json}))

;; (defn insert-record
;;   "Insert percentage info + timestamp in the database"
;;   [i]
;;   (j/insert! db-spec :people
;;              {:ts }))

;; (j/insert! pg-uri :people
;;            {:ts })

(defn -handler
  "Fetches data from the API and stores the record in a database"
  [s]
  (System/getProperty "java.version"))
