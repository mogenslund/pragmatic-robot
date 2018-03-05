(ns pbot.framework.web
  (:require [clojure.string :as str]
            [clojure.test :refer :all]
            [clojure.java.io :as io]))
(import org.openqa.selenium.chrome.ChromeDriver)
(import org.openqa.selenium.chrome.ChromeOptions)
(import org.openqa.selenium.By)
(import org.openqa.selenium.WebDriver)
(import org.openqa.selenium.WebElement)
(import org.openqa.selenium.remote.DesiredCapabilities)
(import org.openqa.selenium.remote.CapabilityType)
(import org.openqa.selenium.logging.LoggingPreferences)
(import org.openqa.selenium.logging.LogType)
(import org.openqa.selenium.remote.RemoteWebDriver)
(import org.openqa.selenium.interactions.Actions)
(import org.openqa.selenium.JavascriptExecutor)
(import org.openqa.selenium.support.ui.WebDriverWait)
(import org.openqa.selenium.support.ui.ExpectedConditions)
(import java.util.concurrent.TimeUnit)
(import java.util.logging.Level)
(import java.util.logging.Logger)
(import java.net.URL)

(def myout (java.io.StringWriter.))

(def browsers (atom []))
(def retries (atom 30))

(defn next-browser
  []
  (swap! browsers #(conj (subvec % 1) (first %))))

(defn web
  []
  (first @browsers))

(defn set-retries
  [sec]
  (reset! retries sec))

(defn new-browser
  "Start a new instance of Chrome."
  []
  (System/setProperty "webdriver.chrome.silentOutput", "true")
  (.setLevel (Logger/getLogger "org.openqa.selenium.remote") Level/OFF)
  (let [options (doto (ChromeOptions.)
                  (.addArguments ["--use-fake-ui-for-media-stream"
                                 ; "--headless"
                                 ; "--use-file-for-fake-audio-capture=/tmp/output.wav"
                                 ]))]
    (swap! browsers conj (ChromeDriver. options))))

(defn sleep ; time in milliseconds
  [s]
  (Thread/sleep s))

(defn quit
  "Closes the browser instance."
  []
  (.quit (web))
  (swap! browsers subvec 1))

(defn quit-all
  []
  (while (> (count @browsers) 0)
    (quit)))

(defn goto
  "Opens url in started Firefox.
  Prepends http:// if not starting with http."
  [url]
  (if (.startsWith url "http")
    (.get (web) url)
    (.get (web) (str "http://" url))))

(defn get-url
  [web]
  (.getCurrentUrl web))

(defn get-element
  "Returns WebElement if present, otherwise nil.
  If the xpath look like an xpath xpath identifier
  is used, otherwise id."
  [xpath]
  (let [elements (.findElements (web) (By/xpath (if (re-find #"/" xpath) xpath (str "//*[@id='" xpath "']"))))
        matches (filter #(.isDisplayed %) elements)]
    (if (= (count matches) 0) ;; If there are no matches return nil
      nil
      (first matches))))      ;; Otherwise return the first element

(defn get-safe
  "Tries up to retries times to
  get the xpath. If no succes
  nil is returned."
  [xpath]
  (loop [i 0 e nil]
    (if (and (< i @retries) (not e))
      (do
        (sleep (if (= i 0) 0 1000))
        (recur (inc i) (get-element xpath)))
      e))) ;; If i>=10 or e not nil


(defn click
  "Clicks a webelement.
  If the element is a string it will be
  considered an xpath."
  [element]
  (let [elem (if (string? element) (get-safe element) element)]
    (when (not elem)
      (quit-all)
      (throw (Exception. (str "\n\nElement: \"" element "\" not found!\n\n"))))
    (.click elem)))

(defn send-keys
  "Takes a WebElement and a string to type into element.
  If the element is a string it will be considered an
  xpath."
  [element keys]
  (let [elem (if (string? element) (get-safe element) element)]
    (when (not elem)
      (quit-all)
      (throw (Exception. (str "\n\nElement: \"" element "\" not found!\n\n"))))
    (.sendKeys elem (into-array [keys]))))

(defn get-text
  "Returns the text content of a WebElement given
  the WebElement itself or an xpath to the WebElement."
  [element]
  (let [elem (if (string? element) (get-safe element) element)]
    (when (not elem)
      (quit-all)
      (throw (Exception. (str "\n\nElement: \"" element "\" not found!\n\n"))))
    (.getText elem)))

(defn wait-for-elements
  "Wait for all elements to be awailable at the
  same time."
  [& elements]
  (loop [n 0]
    (when (= n @retries)
      (quit-all)
      (throw (Exception. (str "\n\nElements: " (str/join ", " (map #(str "\"" % "\"") elements)) " not found!\n\n"))))
    (if (every? identity (map get-element elements))
      true
      (do (sleep 1000)
        (recur (inc n))))))

(defn hover
  "Hover the WebElement."
  [element]
  (let [elem (if (string? element) (get-safe element) element)]
    (-> (Actions. (web))
      (.moveToElement elem)
      (.build)
      (.perform))))

(defn assert-eq
  "Throws an Exception if the two first
  arguments are not equal. In that case
  the given message will be displayed
  as part of the Exception message."
  [observed expected message]
  (when (not= observed expected)
    (quit-all)
    (throw (Exception. (str message "\nObserved: " observed "\nExpected: " expected)))))

(defn testit
  []
  (set-retries 6)
  (new-browser)
  ;(sleep 100)
  (goto "http://wikipedia.org")
  (click "js-link-box-en")
  (send-keys "//input[@type='search']" "Turtle\n")
  ;(hover "p-cactions")
  (assert-eq (get-text "//h1") "Turtle" "Text not found")
  (wait-for-elements "//h1" "//input[@type='search']")
  (quit)
  ;(sleep 200)
  )

(defn testsequence
  []
  (try
    (set-retries 6)
    (new-browser)
    (goto "https://www.walmart.com/")
    (send-keys "global-search-input" "iphone 6s\n")
    (click "//img[contains(@alt,'Apple iPhone 6s')]")
    (assert-eq (get-text "//button[contains(@data-tl-id,'add_to_cart_button')]") "Add to Cart" "Text not found")
    (println "--DONE--")
    (quit)
    (catch Exception e (quit-all) (println e))))

;(doseq [x (range 6)] (testsequence))

;(doseq [x (range 10)] (testit))


; Chromedriver needs to be installed
;{:deps {org.seleniumhq.selenium/selenium-chrome-driver {:mvn/version "3.9.1"}
;        org.seleniumhq.selenium/selenium-support {:mvn/version "3.9.1"}}
; :paths ["/home/mogens/m/lib2"]}