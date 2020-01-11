(defproject human-traffic "0.1.0-SNAPSHOT"
  :description "Backend service to provide JSON data about the amount of people in a certain location at different times. Hosted on AWS Lambda"
  :url "https://github.com/carlosgeos/human-traffic"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[cheshire "5.9.0"]
                 [clj-http "3.10.0"]
                 [clj-sendgrid "0.1.2"]
                 [clojure.java-time "0.3.2"]
                 [com.amazonaws/aws-lambda-java-core "1.2.0"]
                 [congomongo "2.2.0"]
                 [environ "1.1.0"]
                 [failjure "2.0.0"]
                 [funcool/cuerdas "2.2.1"]
                 [org.clojure/clojure "1.10.1"]
                 [prismatic/schema "1.1.12"]]
  :plugins [[lein-environ "1.1.0"]]
  :repl-options {:init-ns human-traffic.core}
  :aot :all                     ;compiles stuff like fetch.clj as well
  :main human-traffic.core)
