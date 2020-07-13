FROM openjdk:11.0.7-jdk

VOLUME /tmp

ARG JAR_FILE

COPY target/${JAR_FILE} payday-ms-auth.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/payday-ms-auth.jar"]