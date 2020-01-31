#install maven
FROM maven:3.6.1 AS build

#owner of the current image
MAINTAINER Kv-061.java

#switch to our package dir
WORKDIR /project_kv_061

COPY . .

RUN mvn -f /project_kv_061/pom.xml clean package

#install light weight jre just for start our .jar file
FROM openjdk:11-jre

WORKDIR /project_kv_061

#copy .jar from
COPY --from=build /project_kv_061/target/project_kv_061-1.0-jar-with-dependencies.jar /project_kv_061-1.0-jar-with-dependencies.jar

EXPOSE 8080
ENTRYPOINT ["java","-jar","/project_kv_061-1.0-jar-with-dependencies.jar"]
#CMD java -jar /target/project_kv_061-1.0-jar-with-dependencies.jar

