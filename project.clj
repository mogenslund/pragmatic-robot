(defproject pragmatic-robot "0.1.0-SNAPSHOT"
  :description "Pragmatic Robot"
  :url ""
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [clj-http "3.8.0"]
                 [org.seleniumhq.selenium/selenium-chrome-driver "3.9.1"]
                 [org.seleniumhq.selenium/selenium-support "3.9.1"]]
  :test-paths ["src"]
  :target-path "target/%s"
  :main pbot.core
  :profiles {:uberjar {:aot :all}
             :liq {:dependencies [[mogenslund/liquid "0.9.2"]]
                   :main dk.salza.liq.core}
             :headless {:jvm-opts ["-Dbrowser=headless"]}
             :chrome {:jvm-opts ["-Dbrowser=chrome"]}
             :slow {:jvm-opts ["-Dslow=true"]}}
  :aliases {"liq" ["with-profile" "liq" "run" "--load=.liq" "--jframe"]
            "headless" ["with-profile" "+headless" "test"]
            "chrome" ["with-profile" "+chrome" "test"]})