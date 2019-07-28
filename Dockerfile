FROM openjdk:11-jdk
RUN apt install -y unzip
COPY build/distributions/*.zip /app.zip
RUN unzip app.zip
RUN cp -R spring-example-0.0.1-SNAPSHOT/* /
ENTRYPOINT /bin/sh -c ./bin/spring-example