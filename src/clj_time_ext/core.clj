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

(defn tstp [pattern]
  (let [
        ;; pattern "HHmmss.nnnn"
        fmt (java.time.format.DateTimeFormatter/ofPattern pattern)
        date (java.time.LocalDateTime/now)]
    (subs (.format date fmt) 0 (count pattern))))

(defn tstp4
  "timestamp 112147.1234"
  [] (tstp "HHmmss.nnnn"))

(defn tnow
  "timestamp 112147.123"
  [] (tstp "HHmmss.nnn"))

(defn fntime [v]
  (tf/unparse (tf/formatter "HH:mm dd.MM.yy")
              (t/date-time v)))
