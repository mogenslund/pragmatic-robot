(ns pbot.shared.walmart
  (:require [pbot.lib.web :refer :all]))

(defn walmart-search
  [s]
  (goto "https://www.walmart.com/")
  (send-keys "global-search-input" (format "%s\n" s)))