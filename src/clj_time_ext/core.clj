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

(defn fmt-modified-ago [datetime-tstp long]
  (let [period (-> (t/interval datetime-tstp (t/now))
                   .toPeriod)
        formatter
        (if long
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
              (.appendSuffix " min, " " mins, ")
              .printZeroNever
              .toFormatter))
        ]
    (str (.print formatter period) (if long " ago" ""))))

(defn modified-ago
  ([datetime-tstp]      (fmt-modified-ago datetime-tstp false))
  ([datetime-tstp long] (fmt-modified-ago datetime-tstp long)))

(defn file-modified-ago
  ([filepath]      (fmt-modified-ago (modified filepath) false))
  ([filepath long] (fmt-modified-ago (modified filepath) long)))

(defn tstp-modified-ago
  ([tstp]      (fmt-modified-ago (new DateTime tstp) false))
  ([tstp long] (fmt-modified-ago (new DateTime tstp) long)))
