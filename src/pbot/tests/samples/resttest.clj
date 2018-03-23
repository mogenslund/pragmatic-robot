(ns pbot.tests.samples.resttest
  (:require [clojure.test :refer :all]
            [clj-http.client :as client]))

(deftest resttest1
  (let [response (client/get "https://google.com")]
    (is (= (response :status) 200))))
