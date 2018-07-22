(ns clj-time-ext.core
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc])
  (:import org.apache.commons.io.FilenameUtils
          org.joda.time.format.PeriodFormatterBuilder
          org.joda.time.DateTime)
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

(defn sfx [fmt k desc-length]
  (let [time-unit (desc-length (k time-unit-desc))]
    (.appendSuffix fmt
                   (first  time-unit)
                   (second time-unit))))

(defn ymw-formatter [desc-length]
  (-> (new PeriodFormatterBuilder)
      .appendYears  (sfx :year  desc-length)
      .appendMonths (sfx :month desc-length)
      .appendWeeks  (sfx :week  desc-length)))

(defn call-cc [fmt test-val cont-fn] (if (pos? (test-val)) fmt (cont-fn fmt)))

(defn builder
  [period {:keys [verbose desc-length] :or {verbose false desc-length :long}}]
  (let [ymw (ymw-formatter desc-length)]
    (if verbose
      (-> ymw
          .appendDays    (sfx :day desc-length)
          .appendHours   (sfx :hour desc-length)
          .appendMinutes (sfx :minute desc-length)
          .appendSeconds (sfx :second desc-length)
          .appendMillis  (sfx :msec desc-length))
      (call-cc
       ymw
       #(.getWeeks period)
       (fn [fmt]
         (call-cc (-> fmt .appendDays (sfx :day desc-length))
                  #(.getDays period)
                  (fn [fmt]
                    (call-cc
                     (-> fmt .appendHours (sfx :hour desc-length))
                     #(.getHours period)
                     (fn [fmt]
                       (call-cc
                        (-> fmt .appendMinutes (sfx :minute desc-length))
                        #(.getMinutes period)
                        (fn [fmt]
                          (call-cc
                           (-> fmt .appendSeconds (sfx :second desc-length))
                           #(.getMilis period)
                           (fn [fmt]
                             (call-cc
                              (-> fmt .appendSeconds (sfx :msec desc-length))
                              #(.getMillis period)
                              (fn [fmt] fmt)))))))))))))))

(defn modified-diff
  "Example:
  (let [datetime-tstp (new DateTime #inst
                         \"2015-07-20T14:26:34.634599000-00:00\")]
  (modified-diff datetime-tstp (t/now) {:verbose true}))"

  [datetime-tstp datetime-tstp-now
   {:keys [verbose desc-length] :or {verbose false desc-length :long} :as prm}]
  (let [period (.toPeriod (t/interval datetime-tstp datetime-tstp-now))
        formatter (-> period
                      (builder prm)
                      .printZeroNever
                      .toFormatter)]
    (str (clojure.string/trimr (.print formatter period))
         (if verbose " ago" ""))))

(defn modified-ago
  "Usage: (modified-ago datetime-tstp {:verbose true :desc-length :short})"
  [datetime-tstp {:keys [verbose desc-length]
                  :or {verbose false desc-length :long} :as prm-map}]
  (modified-diff datetime-tstp (t/now) prm-map))


(defn file-modified-ago
  "Usage: (file-modified-ago filepath {:verbose true :desc-length :short})"
  [filepath {:keys [verbose desc-length]
             :or {verbose false desc-length :long} :as prm-map}]
  (modified-ago (tc/from-long (.lastModified (new java.io.File filepath)))
                prm-map))

(defn tstp-modified-ago
  "Usage: (tstp-modified-ago tstp { :verbose true :desc-length :short})"
  [tstp {:keys [verbose desc-length]
         :or {verbose false desc-length :long} :as prm-map}]
  (modified-ago (new DateTime tstp) prm-map))
