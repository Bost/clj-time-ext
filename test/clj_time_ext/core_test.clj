(ns clj-time-ext.core-test
  (:require [clojure.test :refer :all]
            [clj-time-ext.core :refer :all])
  (:import org.apache.commons.io.FilenameUtils
           org.joda.time.format.PeriodFormatterBuilder
           org.joda.time.DateTime))

(deftest basic
  (testing "basic - FIXME, I fail."
    (is (.equals
         "2 days 1 second ago"
         (modified-diff
          (new DateTime #inst "2015-07-20T14:26:34.634599000-00:00")
          (new DateTime #inst "2015-07-22T14:26:35.634599000-00:00")
          {:verbose true :desc-length :long})))
    (is (.equals
         "2d1s ago"
         (modified-diff
          (new DateTime #inst "2015-07-20T14:26:34.634599000-00:00")
          (new DateTime #inst "2015-07-22T14:26:35.634599000-00:00")
          {:verbose true :desc-length :short})))
    (is (.equals
         "2 days"
         (modified-diff
          (new DateTime #inst "2015-07-20T14:26:34.634599000-00:00")
          (new DateTime #inst "2015-07-22T14:26:35.634599000-00:00")
          {:verbose false :desc-length :long})))
    (is (.equals
         "2d"
         (modified-diff
          (new DateTime #inst "2015-07-20T14:26:34.634599000-00:00")
          (new DateTime #inst "2015-07-22T14:26:35.634599000-00:00")
          {:verbose false :desc-length :short})))))
