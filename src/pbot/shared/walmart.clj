(ns pbot.shared.walmart
  (:require [pbot.framework.web :refer :all]))

(defn walmart-search
  [s]
  (goto "https://www.walmart.com/")
  (send-keys "global-search-input" (format "%s\n" s)))