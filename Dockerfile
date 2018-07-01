FROM maven:alpine

RUN mkdir -p /tmp/project
ADD . /tmp/project

RUN cd /tmp/project && \
    mvn install && \
    rm -rf /tmp/project

WORKDIR /tmp/project