(ns clj-time-ext.core
  (:require [clj-time.core :as t]
            [clj-time.coerce :as tc])
  (import org.apache.commons.io.FilenameUtils
          org.joda.time.format.PeriodFormatterBuilder
          org.joda.time.DateTime)
  (:gen-class))

(defn modified [filepath]
  (let [m (.lastModified (new java.io.File filepath))]
    (tc/from-long m)))

(defn modified-ago
  "Usage: (modified-ago datetime-tstp :verbose true)"
  [datetime-tstp & {:keys [verbose] :or {verbose false}}]
  (let [period (-> (t/interval datetime-tstp (t/now))
                   .toPeriod)
        formatter
        (if verbose
          (-> (new PeriodFormatterBuilder)
              .appendYears
              (.appendSuffix " year, " " years, ")
              .appendMonths
              (.appendSuffix " month, " " months, ")
              .appendWeeks
              (.appendSuffix " week, " " weeks, ")
              .appendDays
              (.appendSuffix " day, " " days, ")
              .appendHours
              (.appendSuffix " hour, " " hours, ")
              .appendMinutes
              (.appendSuffix " minute, " " minutes, ")
              .appendSeconds
              (.appendSuffix " second" " seconds")
              .printZeroNever
              .toFormatter)
          (-> (new PeriodFormatterBuilder)
              .appendYears
              (.appendSuffix " year " " years ")
              .appendMonths
              (.appendSuffix " month " " months ")
              .appendWeeks
              (.appendSuffix " week " " weeks ")
              .appendDays
              (.appendSuffix " day " " days ")
              .appendHours
              (.appendSuffix " hour " " hours ")
              .appendMinutes
              (.appendSuffix " min" " mins")
              .printZeroNever
              .toFormatter))
        ]
    (str (.print formatter period) (if verbose " ago" ""))))

(defn file-modified-ago
  "Usage: (file-modified-ago filepath :verbose true)"
  [filepath & {:keys [verbose] :or {verbose false}}]
  (modified-ago (modified filepath) :verbose verbose))

(defn tstp-modified-ago
  "Usage: (tstp-modified-ago tstp :verbose true)"
  [tstp & {:keys [verbose] :or {verbose false}}]
  (modified-ago (new DateTime tstp) :verbose verbose))
