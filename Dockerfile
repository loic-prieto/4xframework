#
# 4X Framework - A framework to develop turn based strategy games
# Copyright © 2018 Loïc Prieto Dehennault (loic.sephiroth@gmail.com)
#
# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU Affero General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU Affero General Public License for more details.
#
# You should have received a copy of the GNU Affero General Public License
# along with this program.  If not, see <http://www.gnu.org/licenses/>.
#

# This is the dockerfile for the console client along with the standard plugins loaded into it.
# It builds everything from the ground up, except the retrieval of maven dependencies, which is
# done in the image this one is based on.
# Once the image is generated, it can be launched, binding port 8000 to debug the application.
FROM 4xframework-mvn-dependencies:latest

RUN mkdir -p /tmp/project
ADD . /tmp/project
RUN cd /tmp/project && \
    mvn clean package -Dmaven.test.skip=true -pl "!console-client"

FROM openjdk:14-alpine
RUN mkdir -p /tmp/project/classpath
WORKDIR /tmp/project

# Include the client dependencies in the classpath folder and the standard plugins in the plugins folder
COPY --from=0 /tmp/project/console-client/target/classpath ./classpath
COPY --from=0 /tmp/project/4x-plugin-standard/target/4x-plugin-standard*.jar ./plugins/
COPY --from=0 /tmp/project/4x-plugin-standard-terminal-client/target/4x-plugin*.jar ./plugins/
COPY --from=0 /tmp/project/4x-plugin-civilization/target/4x-plugin*.jar ./plugins/
COPY --from=0 /tmp/project/4x-plugin-civilization-terminal-client/target/4x-plugin*.jar ./plugins/

# Launch the client with the standard plugin loaded on debug mode
CMD java -agentlib:jdwp=transport=dt_socket,server=y,suspend=n,address=*:8000 \
    -classpath "classpath/*:plugins/*" \
    org.sephire.games.framework4x.clients.terminal.Launcher

# Debug port
EXPOSE 8000
