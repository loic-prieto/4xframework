FROM maven:3.6.0-jdk-11-slim

RUN mkdir -p /tmp/project
ADD . /tmp/project
RUN cd /tmp/project && \
    mvn clean package -Dmaven.test.skip=true


FROM openjdk:11-slim
RUN mkdir -p /tmp/project
WORKDIR /tmp/project
COPY --from=0 /tmp/project/console-client/target/modules ./modules
CMD java --module-path modules -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 \
    --module org.sephire.games.framework4x.clients.terminal/org.sephire.games.framework4x.clients.terminal.Launcher

EXPOSE 8000