(defproject clj-time-ext "0.7.2"
  :description "Extention of clj-time"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [commons-io/commons-io "2.5"]
                 [clj-time "0.12.2"]]
  :main clj-time-ext.core
  :profiles {:uberjar {:aot :all}})
