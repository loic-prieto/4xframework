# This is the dockerfile for the console client along with the standard plugins loaded into it.
# It builds everything from the ground up, except the retrieval of maven dependencies, which is
# done in the image this one is based on.
# Once the image is generated, it can be launched, binding port 8000 to debug the application.
FROM 4xframework-mvn-dependencies:latest

RUN mkdir -p /tmp/project
ADD . /tmp/project
RUN cd /tmp/project && \
    mvn clean package -Dmaven.test.skip=true

FROM openjdk:11-slim
RUN mkdir -p /tmp/project/classpath
WORKDIR /tmp/project

# Include the client dependencies in the classpath folder and the standard plugins in the plugins folder
COPY --from=0 /tmp/project/console-client/target/classpath ./classpath
COPY --from=0 /tmp/project/4x-plugin-standard/target/4x-plugin-standard*.jar ./plugins/
COPY --from=0 /tmp/project/4x-plugin-standard-terminal-client/target/4x-plugin*.jar ./plugins/

# Launch the client with the standard plugin loaded on debug mode
CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 \
    -classpath "classpath/*:plugins/*" \
    org.sephire.games.framework4x.clients.terminal.Launcher

# Debug port
EXPOSE 8000