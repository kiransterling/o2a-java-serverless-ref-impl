#!/bin/sh
docker build . -t o2a-java-serverless-ref-impl
echo
echo
echo "To run the docker container execute:"
echo "    $ docker run -p 8080:8080 o2a-java-serverless-ref-impl"
