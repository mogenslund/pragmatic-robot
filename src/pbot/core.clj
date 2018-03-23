(ns pbot.core
  (:require [clojure.test :refer :all]
            [pbot.tests.samples.sample1]
            [pbot.tests.samples.sample2]
            [pbot.tests.samples.manytests]
            [pbot.tests.samples.resttest]))

(defn -main
  [& args]
  (cond (some #(= "--all" %) args) (run-all-tests #"pbot.tests.*")
        (some #(= "--samples" %) args) (run-all-tests #"pbot.tests.samples.sample.*")
        (some #(= "--rest" %) args) (run-all-tests #"pbot.tests.samples.rest.*")
        true (println "--all\n--samples\n--rest")))