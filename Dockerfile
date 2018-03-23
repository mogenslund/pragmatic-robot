# docker build -t pbot .
# docker run --name=pbot --rm=true pbot
FROM ubuntu

MAINTAINER Mogens Lund <salza@salza.dk>

WORKDIR /tmp

RUN apt-get -y update

RUN apt-get install -y curl
RUN apt-get install -y default-jre
RUN apt-get install -y unzip
RUN apt-get install -y wget
RUN apt-get install -y google-chrome-stable

RUN curl -O https://download.clojure.org/install/linux-install-1.9.0.358.sh \
  && chmod +x linux-install-1.9.0.358.sh \
  && ./linux-install-1.9.0.358.sh

RUN wget https://chromedriver.storage.googleapis.com/2.36/chromedriver_linux64.zip
RUN unzip chromedriver_linux64.zip
RUN mv -f chromedriver /usr/local/bin/chromedriver

COPY ./deps.edn .
RUN clojure -e "(println :Loading :deps)"

RUN mkdir -p /workspace
WORKDIR /workspace/


ADD . .

ENTRYPOINT ./runall.sh