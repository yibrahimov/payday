FROM openjdk:11.0.7-jdk

VOLUME /tmp

ARG JAR_FILE

COPY target/${JAR_FILE} payday-ms-user.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/payday-ms-user.jar"]