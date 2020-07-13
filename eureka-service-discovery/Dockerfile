FROM openjdk:11.0.7-jdk

VOLUME /tmp

ARG JAR_FILE

COPY target/${JAR_FILE} eureka-service-discovery.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/eureka-service-discovery.jar"]