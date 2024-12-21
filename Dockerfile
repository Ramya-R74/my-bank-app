# Multi-stage docker file
FROM maven:3.8.3-openjdk-17 as builder
# metadata
MAINTAINER Ramya R <ramyawork22@gmail.com>
LABEL app=bankapp

# working directory creation 

WORKDIR /src

COPY . /src

RUN mvn clean install -DskipTests=true

#-------------Stage 2 ---------------------

# building light weight image

FROM openjdk:17-alpine as deployer

COPY --from=builder /src/target/*.jar /src/target/bankapp.jar

EXPOSE 8080

CMD [ "java", "-jar", "/src/target/bankapp.jar" ]

