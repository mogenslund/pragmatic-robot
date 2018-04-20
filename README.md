# README

## Leiningen
lein run --rest
lein run --all

## LEIN REPL
Command:

    lein repl

Example:

    (ns pbot.lib.web) 
    (new-browser)
    (goto "http://google.com")
    (click "//input[@name='btnI']")
    (quit-all)

Running tests from repl:

    (run-tests 'pbot.tests.samples.resttest)
    (run-tests 'pbot.tests.samples.sample1 'pbot.tests.samples.sample2)
    (run-all-tests #"pbot.tests.samples.sample.*")