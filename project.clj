(defproject clj-time-ext "0.6.0"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [commons-io/commons-io "2.4"]
                 [clj-time "0.11.0"]]
  :main clj-time-ext.core
  :profiles {:uberjar {:aot :all}})
