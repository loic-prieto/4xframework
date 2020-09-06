# This program is free software: you can redistribute it and/or modify
# it under the terms of the GNU General Public License as published by
# the Free Software Foundation, either version 3 of the License, or
# (at your option) any later version.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program.  If not, see <https://www.gnu.org/licenses/>.

# Build the base image upon which the runtime image is built. This is done so that we don't redownload maven dependencies
# each time, a time consuming process.
# Unless dependencies are changed, there's no need to run this command more than once, when creating the image.
build-deps:
	docker build -f Dockerfile-build-dependencies -t 4xframework-mvn-dependencies:latest .

# Build the runtime image to launch the application, should be invoked each time there's a code change in the main
# code or its plugins.
# Will package the standard plugins as defined in this project
build:
	docker build -t 4x-client .

# Run the already built client image with the console client packaged along the standard plugins
run:
	docker run -v $(HOME):/root --name 4xclient -p 8000:8000 -it --rm 4x-client:latest
