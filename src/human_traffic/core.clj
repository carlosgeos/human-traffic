(ns human-traffic.core
  (:gen-class
   :methods [^:static [handler [String] String]]))


(defn -handler [s]
  (let [time_now (t/to-time-zone (t/now) (t/time-zone-for-id "Europe/Brussels")) ]
    (str "Hello " s "! " "The time is now: "
         (t/hour time_now) ":" (t/minute time_now))))
