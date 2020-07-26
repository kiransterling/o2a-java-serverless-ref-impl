FROM oracle/graalvm-ce:20.1.0-java8 as graalvm
RUN gu install native-image

COPY . /home/app/o2a-java-serverless-ref-impl
WORKDIR /home/app/o2a-java-serverless-ref-impl

RUN native-image --no-server -cp target/o2a-java-serverless-ref-impl-*.jar

FROM frolvlad/alpine-glibc
RUN apk update && apk add libstdc++
EXPOSE 8080
COPY --from=graalvm /home/app/o2a-java-serverless-ref-impl/o2a-java-serverless-ref-impl /app/o2a-java-serverless-ref-impl
ENTRYPOINT ["/app/o2a-java-serverless-ref-impl"]
