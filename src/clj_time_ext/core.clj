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

(defn modified-ago [datetime-tstp]
  (let [period (-> (t/interval datetime-tstp (t/now))
                   .toPeriod)
        formatter
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
            .toFormatter)]
    (str (.print formatter period) " ago")))

(defn file-modified-ago [filepath]
  (modified-ago (modified filepath)))
  
(defn tstp-modified-ago [tstp]
  (modified-ago (new DateTime tstp)))
  
