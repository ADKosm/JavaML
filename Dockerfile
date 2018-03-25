FROM ubuntu:16.04

ADD . /app/

WORKDIR /app/

RUN apt-get update -y && apt-get install software-properties-common python-software-properties debconf-utils -y
RUN add-apt-repository ppa:webupd8team/java -y && apt-get update -y && \
    echo "oracle-java8-installer shared/accepted-oracle-license-v1-1 select true" | debconf-set-selections && \
    apt-get install oracle-java8-installer -y
RUN apt-get install wget tar
RUN /app/gradlew

CMD ['bash']
