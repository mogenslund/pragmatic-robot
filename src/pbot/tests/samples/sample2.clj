(ns pbot.tests.samples.sample1
  (:require [clojure.test :refer :all]
            [pbot.shared.walmart :refer :all]
            [pbot.lib.web :refer :all]))

(deftest searchtest1
  (set-retries 6)
  (new-browser)
  (walmart-search "iphone 6s")
  (click "//img[contains(@alt,'Apple iPhone 6s')]")
  (is (= (get-text "//button[contains(@data-tl-id,'add_to_cart_button')]") "Add to Cart") "Text not found")
  (quit))

(deftest searchtest2
  (set-retries 6)
  (new-browser)
  (goto "https://www.walmart.com/")
  (send-keys "global-search-input" "iphone 6s\n")
  (click "//img[contains(@alt,'Apple iPhone 6s')]")
  (is (= (get-text "//button[contains(@data-tl-id,'add_to_cart_button')]") "Add to Cart") "Text not found")
  (quit))

(deftest good
  (is (= 2 (+ 1 1)) "Not working"))



