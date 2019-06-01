FROM openjdk:13-jdk-oracle
VOLUME /tmp
COPY build/libs/*.jar /app.jar
RUN sh -c 'touch /app.jar'
EXPOSE 8080
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app.jar" ]
