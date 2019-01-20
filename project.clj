(defproject human-traffic "0.1.0-SNAPSHOT"
  :description "Backend service to provide JSON data about the amount of people in a certain location at different times. Hosted on AWS Lambda"
  :url "https://github.com/carlosgeos/human-traffic"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [com.amazonaws/aws-lambda-java-core "1.2.0"]
                 [clojure.java-time "0.3.2"]
                 [org.clojure/data.json "0.2.6"]
                 [cheshire "5.8.1"]
                 [clj-http "3.9.1"]
                 [environ "1.1.0"]
                 [org.clojure/java.jdbc "0.7.8"]
                 [org.postgresql/postgresql "42.2.5"]]
  :plugins [[lein-environ "1.1.0"]]
  :repl-options {:init-ns human-traffic.core}
  :aot :all                     ;compiles stuff like fetch.clj as well
  :main human-traffic.core)
