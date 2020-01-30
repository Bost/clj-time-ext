(defproject clj-time-ext :lein-v
  :description
  "Consider using
[org.ocpsoft.prettytime/prettytime \"...\"]
https://mvnrepository.com/artifact/org.ocpsoft.prettytime/prettytime"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.10.1"]
                 [clj-time "0.15.2"]
                 [com.darwinsys/hirondelle-date4j "1.5.1"]]
  ;; can't use the lein-v from ~/.lein/profiles.clj
  :plugins [[com.roomkey/lein-v "7.1.0"]]
  :main clj-time-ext.core
  :profiles {:uberjar {:aot :all}}
  ;; enter user/password on the command line
  :deploy-repositories [["clojars" {:url "https://repo.clojars.org"
                                    :sign-releases false}]]
  )
