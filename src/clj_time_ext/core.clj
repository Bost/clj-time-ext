(ns clj-time-ext.core
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc])
  (:import org.apache.commons.io.FilenameUtils
          org.joda.time.format.PeriodFormatterBuilder
          org.joda.time.DateTime)
  (:gen-class))

(defn modified [filepath]
  (let [m (.lastModified (new java.io.File filepath))]
    (tc/from-long m)))

(def time-unit-desc
  {:year   {:long [" year" " years "]     :short ["y" "y"]}
   :month  {:long [" month" " months "]   :short ["m" "m"]}
   :week   {:long [" week" " weeks "]     :short ["w" "w"]}
   :day    {:long [" day" " days "]       :short ["d" "d"]}
   :hour   {:long [" hour" " hours "]     :short ["h" "h"]}
   :minute {:long [" minute" " minutes "] :short ["m" "m"]}
   :second {:long [" second" " seconds "] :short ["s" "s"]}})

(defn modified-diff
  "Typical usage: (modified-diff datetime-tstp (t/now) :verbose true)"
  [datetime-tstp datetime-tstp-now
   {:keys [verbose desc-length] :or {verbose false desc-length :long}}]
  (let [period (-> (t/interval datetime-tstp
                               datetime-tstp-now)
                   .toPeriod)
        prd-frmt-builder (new PeriodFormatterBuilder)
        builder
        (if verbose
          (-> prd-frmt-builder
              .appendYears
              (.appendSuffix (nth (->> time-unit-desc :year desc-length) 0)
                             (nth (->> time-unit-desc :year desc-length) 1))
              .appendMonths
              (.appendSuffix (nth (->> time-unit-desc :month desc-length) 0)
                             (nth (->> time-unit-desc :month desc-length) 1))
              .appendWeeks
              (.appendSuffix (nth (->> time-unit-desc :week desc-length) 0)
                             (nth (->> time-unit-desc :week desc-length) 1))
              .appendDays
              (.appendSuffix (nth (->> time-unit-desc :day desc-length) 0)
                             (nth (->> time-unit-desc :day desc-length) 1))
              .appendHours
              (.appendSuffix (nth (->> time-unit-desc :hour desc-length) 0)
                             (nth (->> time-unit-desc :hour desc-length) 1))
              .appendMinutes
              (.appendSuffix (nth (->> time-unit-desc :minute desc-length) 0)
                             (nth (->> time-unit-desc :minute desc-length) 1))
              .appendSeconds
              (.appendSuffix (nth (->> time-unit-desc :second desc-length) 0)
                             (nth (->> time-unit-desc :second desc-length) 1)))
          (if (> (.getMonths period) 0)
            (-> prd-frmt-builder
                .appendYears
                (.appendSuffix (nth (->> time-unit-desc :year desc-length) 0)
                               (nth (->> time-unit-desc :year desc-length) 1))
                .appendMonths
                (.appendSuffix (nth (->> time-unit-desc :month desc-length) 0)
                               (nth (->> time-unit-desc :month desc-length) 1))
                .appendWeeks
                (.appendSuffix (nth (->> time-unit-desc :week desc-length) 0)
                               (nth (->> time-unit-desc :week desc-length) 1)))
            (if (> (.getWeeks period) 0)
              (-> prd-frmt-builder
                  .appendYears
                  (.appendSuffix (nth (->> time-unit-desc :year desc-length) 0)
                                 (nth (->> time-unit-desc :year desc-length) 1))
                  .appendMonths
                  (.appendSuffix (nth (->> time-unit-desc :month desc-length) 0)
                                 (nth (->> time-unit-desc :month desc-length) 1))
                  .appendWeeks
                  (.appendSuffix (nth (->> time-unit-desc :week desc-length) 0)
                                 (nth (->> time-unit-desc :week desc-length) 1))
                  .appendDays
                  (.appendSuffix (nth (->> time-unit-desc :day desc-length) 0)
                                 (nth (->> time-unit-desc :day desc-length) 1)))
              (if (> (.getDays period) 0)
                (-> prd-frmt-builder
                    .appendYears
                    (.appendSuffix (nth (->> time-unit-desc :year desc-length) 0)
                                   (nth (->> time-unit-desc :year desc-length) 1))
                    .appendMonths
                    (.appendSuffix (nth (->> time-unit-desc :month desc-length) 0)
                                   (nth (->> time-unit-desc :month desc-length) 1))
                    .appendWeeks
                    (.appendSuffix (nth (->> time-unit-desc :week desc-length) 0)
                                   (nth (->> time-unit-desc :week desc-length) 1))
                    .appendDays
                    (.appendSuffix (nth (->> time-unit-desc :day desc-length) 0)
                                   (nth (->> time-unit-desc :day desc-length) 1))
                    .appendHours
                    (.appendSuffix (nth (->> time-unit-desc :hour desc-length) 0)
                                   (nth (->> time-unit-desc :hour desc-length) 1)))
                (if (> (.getHours period) 0)
                  (-> prd-frmt-builder
                      .appendYears
                      (.appendSuffix (nth (->> time-unit-desc :year desc-length) 0)
                                     (nth (->> time-unit-desc :year desc-length) 1))
                      .appendMonths
                      (.appendSuffix (nth (->> time-unit-desc :month desc-length) 0)
                                     (nth (->> time-unit-desc :month desc-length) 1))
                      .appendWeeks
                      (.appendSuffix (nth (->> time-unit-desc :week desc-length) 0)
                                     (nth (->> time-unit-desc :week desc-length) 1))
                      .appendDays
                      (.appendSuffix (nth (->> time-unit-desc :day desc-length) 0)
                                     (nth (->> time-unit-desc :day desc-length) 1))
                      .appendHours
                      (.appendSuffix (nth (->> time-unit-desc :hour desc-length) 0)
                                     (nth (->> time-unit-desc :hour desc-length) 1))
                      .appendMinutes
                      (.appendSuffix (nth (->> time-unit-desc :minute desc-length) 0)
                                     (nth (->> time-unit-desc :minute desc-length) 1)))
                  (-> prd-frmt-builder
                      .appendYears
                      (.appendSuffix (nth (->> time-unit-desc :year desc-length) 0)
                                     (nth (->> time-unit-desc :year desc-length) 1))
                      .appendMonths
                      (.appendSuffix (nth (->> time-unit-desc :month desc-length) 0)
                                     (nth (->> time-unit-desc :month desc-length) 1))
                      .appendWeeks
                      (.appendSuffix (nth (->> time-unit-desc :week desc-length) 0)
                                     (nth (->> time-unit-desc :week desc-length) 1))
                      .appendDays
                      (.appendSuffix (nth (->> time-unit-desc :day desc-length) 0)
                                     (nth (->> time-unit-desc :day desc-length) 1))
                      .appendHours
                      (.appendSuffix (nth (->> time-unit-desc :hour desc-length) 0)
                                     (nth (->> time-unit-desc :hour desc-length) 1))
                      .appendMinutes
                      (.appendSuffix (nth (->> time-unit-desc :minute desc-length) 0)
                                     (nth (->> time-unit-desc :minute desc-length) 1))
                      .appendSeconds
                      (.appendSuffix (nth (->> time-unit-desc :second desc-length) 0)
                                     (nth (->> time-unit-desc :second desc-length) 1))))))))
        formatter (-> builder
                .printZeroNever
                .toFormatter)]
    (str (clojure.string/trimr (.print formatter period))
         (if verbose " ago" ""))))

(defn modified-ago
  "Usage: (modified-ago datetime-tstp { :verbose true :desc-length :short })"
  [datetime-tstp {:keys [verbose desc-length]
                  :or {verbose false desc-length :long} :as prm-map}]
  (modified-diff datetime-tstp (t/now) prm-map))

(defn file-modified-ago
  "Usage: (file-modified-ago filepath { :verbose true :desc-length :short })"
  [filepath {:keys [verbose desc-length]
             :or {verbose false desc-length :long} :as prm-map}]
  (modified-ago (modified filepath) prm-map))

(defn tstp-modified-ago
  "Usage: (tstp-modified-ago tstp { :verbose true :desc-length :short })"
  [tstp {:keys [verbose desc-length]
         :or {verbose false desc-length :long} :as prm-map}]
  (modified-ago (new DateTime tstp) prm-map))
