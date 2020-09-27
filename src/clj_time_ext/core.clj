(ns clj-time-ext.core
  (:require
   [clj-time.core :as t]
   [clj-time.coerce :as tc]
   [clj-time.format :as tf])
  (:import
   org.joda.time.format.PeriodFormatterBuilder
   org.joda.time.DateTime
   java.util.Locale
   java.util.TimeZone
   )
  (:gen-class))

(def time-unit-desc
  {:year   {:long [" year "   " years "  ] :short ["y" "y"]}
   :month  {:long [" month "  " months " ] :short ["m" "m"]}
   :week   {:long [" week "   " weeks "  ] :short ["w" "w"]}
   :day    {:long [" day "    " days "   ] :short ["d" "d"]}
   :hour   {:long [" hour "   " hours "  ] :short ["h" "h"]}
   :minute {:long [" minute " " minutes "] :short ["m" "m"]}
   :second {:long [" second " " seconds "] :short ["s" "s"]}
   :msec   {:long [" msecs "  " msec "   ] :short ["ms" "ms"]}
   })

(defn sfx
  ".appendSuffix fmt ..."
  [fmt k desc-length]
  (let [time-unit (desc-length (k time-unit-desc))]
    (.appendSuffix fmt
                   (first  time-unit)
                   (second time-unit))))

(defn ymw-formatter [desc-length]
  (-> (new PeriodFormatterBuilder)
      .appendYears  (sfx :year  desc-length)
      .appendMonths (sfx :month desc-length)
      .appendWeeks  (sfx :week  desc-length)))

(defn cps
  "Continuation Passing Style"
  [fmt test-val cont-fn] (if (pos? (test-val)) fmt (cont-fn fmt)))

(defn builder
  "TODO try to use continuation monad m-cont"
  [period {:keys [verbose desc-length] :or {verbose false desc-length :long}}]
  (let [ymw (ymw-formatter desc-length)]
    (if verbose
      (-> ymw
          .appendDays    (sfx :day desc-length)
          .appendHours   (sfx :hour desc-length)
          .appendMinutes (sfx :minute desc-length)
          .appendSeconds (sfx :second desc-length)
          .appendMillis  (sfx :msec desc-length))
      (cps
       ymw
       #(.getWeeks period)
       (fn [fmt]
         (cps (-> fmt .appendDays (sfx :day desc-length))
              #(.getDays period)
              (fn [fmt]
                (cps
                 (-> fmt .appendHours (sfx :hour desc-length))
                 #(.getHours period)
                 (fn [fmt]
                   (cps
                    (-> fmt .appendMinutes (sfx :minute desc-length))
                    #(.getMinutes period)
                    (fn [fmt]
                      (cps
                       (-> fmt .appendSeconds (sfx :second desc-length))
                       #(.getSeconds period)
                       (fn [fmt]
                         (cps
                          (-> fmt .appendMillis (sfx :msec desc-length))
                          #(.getMillis period)
                          (fn [fmt] fmt)))))))))))))))

(defn intervall-diff
  "e.g. (intervall-diff (new DateTime #inst
                         \"2015-07-20T14:26:34.634599000-00:00\") (t/now)})"
  [dt-a dt-b prm]
  (let [period (.toPeriod (t/interval dt-a dt-b))
        formatter (-> period
                      (builder prm)
                      .printZeroNever
                      .toFormatter)]
    (clojure.string/trimr (.print formatter period))))

(defn ago-diff
  "e.g. (ago-diff dt-a {:verbose true :desc-length :short})"
  [dt-a {:keys [verbose desc-length]
         :or {verbose false desc-length :long} :as prm-map}]
  (str (intervall-diff dt-a (t/now) prm-map)
       (if verbose " ago" "")))

(defn tstp-ago-diff
  "e.g. (tstp-ago-diff tstp {:verbose true :desc-length :short})"
  [tstp {:keys [verbose desc-length]
         :or {verbose false desc-length :long} :as prm-map}]
  (ago-diff (new DateTime tstp) prm-map))

(defn tstp
  "Return a string with current timestamp, formatted using `pattern` and shortened
  to the length of the `pattern` for a given timezone. E.g.:
  (tstp \"HHmmss.nnn\")
  ;; => \"161644.809\"
  (tstp \"HHmmss.nnn\" \"Europe/London\")
  ;; => \"151644.809\"
  (tstp \"HHmmss.nnn\" \"Europe/Berlin\")
  ;; => \"161644.809\"
  "
  ([] (tstp "HHmmss.nnn" (str (java.time.ZoneId/systemDefault))))
  ([pattern] (tstp pattern (str (java.time.ZoneId/systemDefault))))
  ([pattern zone-id]
   (let [
         ;; pattern "HHmmss.nnnn"
         fmt (java.time.format.DateTimeFormatter/ofPattern pattern)
         date (java.time.LocalDateTime/now (java.time.ZoneId/of zone-id))]
     (subs (.format date fmt) 0 (count pattern)))))

(defn tstp3
  "Return a string with current 3-digits timestamp. E.g.:
  (tstp)
  ;; => 112147.123"
  [] (tstp))

(defn tstp4
  "Return a string with current 4-digits timestamp. E.g.:
  (tstp \"HHmmss.nnnn\")
  ;; => 112147.1234"
  [] (tstp "HHmmss.nnnn"))

(defn tstp5
  "Return a string with current 5-digits timestamp. E.g.:
  (tstp \"HHmmss.nnnnn\")
  ;; => 112147.12345"
  [] (tstp "HHmmss.nnnnn"))

(defn tstp6
  "Return a string with current 6-digits timestamp. E.g.:
  (tstp \"HHmmss.nnnnnn\")
  ;; => 112147.123456"
  [] (tstp "HHmmss.nnnnnn"))

(defn tstp7
  "Return a string with current 7-digits timestamp. E.g.:
  (tstp \"HHmmss.nnnnnnn\")
  ;; => 112147.1234567"
  [] (tstp "HHmmss.nnnnnnn"))

(defn tstp8
  "Return a string with current 8-digits timestamp. E.g.:
  (tstp \"HHmmss.nnnnnnnn\")
  ;; => 112147.12345678"
  [] (tstp "HHmmss.nnnnnnnn"))

(defn tstp9
  "Return a string with current 9-digits timestamp. E.g.:
  (tstp \"HHmmss.nnnnnnnnn\")
  ;; => 112147.123456789"
  [] (tstp "HHmmss.nnnnnnnnn"))

(defn tnow
  "Return a string containing current timestamp. E.g.
  (tnow)
  ;; => \"161644.809\"
  (tnow \"Europe/London)
  ;; => \"151644.809\"
  "
  ([] (tstp3))
  ([zone-id] (tstp "HHmmss.nnn" zone-id)))

(defn fntime [v]
  (tf/unparse (tf/formatter "HH:mm dd.MM.yy")
              (t/date-time v)))
