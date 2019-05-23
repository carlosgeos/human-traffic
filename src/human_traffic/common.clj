(ns human-traffic.common
  (:gen-class)
  (:require [environ.core :refer [env]]))

(def db (str (env :database-url)
             "?ssl=true&sslfactory=org.postgresql.ssl.NonValidatingFactory"))
