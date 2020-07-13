FROM openjdk:11.0.7-jdk

VOLUME /tmp

ARG JAR_FILE

COPY target/${JAR_FILE} payday-ms-notification.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/payday-ms-notification.jar"]