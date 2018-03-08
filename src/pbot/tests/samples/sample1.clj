(ns pbot.tests.samples.sample1
  (:require [clojure.test :refer :all]
            [pbot.framework.web :refer :all]))

(deftest add
  (is (= 1 (+ 0 1)) "Not working"))

(deftest not-so-good
  (is (= 1 (+ 1 0)) "Not working"))
