(ns pbot.core
  (:require [clojure.test :refer :all]
            [pbot.tests.samples.sample1]
            [pbot.tests.samples.sample2]))

(defn -main
  [& args]
  (run-all-tests #"pbot.tests.*"))