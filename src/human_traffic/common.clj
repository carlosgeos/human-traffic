(ns human-traffic.common
  (:gen-class)
  (:require [environ.core :refer [env]]
            [somnium.congomongo :as m]))

(def sg-api-token (str "Bearer " (env :sendgrid-api-token)))
(def conn
  (m/make-connection (str (env :database-url))))
