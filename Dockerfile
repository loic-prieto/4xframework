FROM maven:3.6.0-jdk-11-slim

RUN mkdir -p /tmp/project
ADD . /tmp/project
RUN cd /tmp/project && \
    mvn clean package -Dmaven.test.skip=true


FROM openjdk:11-slim
RUN mkdir -p /tmp/project/classpath
WORKDIR /tmp/project

# Include the client dependencies and also the standard plugin
COPY --from=0 /tmp/project/console-client/target/modules ./classpath
COPY --from=0 /tmp/project/4x-plugin-standard/target/4x-plugin-standard*.jar ./classpath
COPY --from=0 /tmp/project/4x-plugin-standard-terminal-client/target/4x-plugin*.jar ./classpath

# Launch the client with the standard plugin loaded on debug mode
CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 \
    -classpath "classpath/*" \
    org.sephire.games.framework4x.clients.terminal.Launcher

# Debug port
EXPOSE 8000