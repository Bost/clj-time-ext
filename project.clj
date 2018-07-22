(defproject clj-time-ext "0.16.0"
  :description "Extention of clj-time"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [commons-io/commons-io "2.6"]
                 [clj-time "0.14.4"]]
  :main clj-time-ext.core
  :profiles {:uberjar {:aot :all}})
