(defproject clj-time-ext :lein-v
  :description
  "Consider using
[org.ocpsoft.prettytime/prettytime \"...\"] or higher
https://mvnrepository.com/artifact/org.ocpsoft.prettytime/prettytime"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.0"]
                 [clj-time "0.15.1"]]
  :plugins [[com.roomkey/lein-v "7.0.0"]]
  :main clj-time-ext.core
  :profiles {:uberjar {:aot :all}})
