FROM openjdk:8-jdk-alpine
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} flightboot.jar
ENTRYPOINT ["java","-jar","/flightboot.jar"]