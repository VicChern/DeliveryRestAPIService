#install maven
FROM maven:3.6.1 AS build

#owner of the current image
MAINTAINER Kv-061.java

#switch to our package dir
WORKDIR /project_kv_061

#copy everything to the container
COPY . .

#Build our project
RUN mvn -f /project_kv_061/pom.xml clean package

#install light weight jre just for start our .jar file
FROM openjdk:11-jre

#switch to working directory
WORKDIR /project_kv_061

#Copy already builded .jar to the root
COPY --from=build /project_kv_061/target/project_kv_061-1.0-jar-with-dependencies.jar /project_kv_061-1.0-jar-with-dependencies.jar

#copy our fronend resources
COPY --from=build /project_kv_061/target-frontend /project_kv_061/target-frontend

EXPOSE 8080

#start our app inside container
ENTRYPOINT ["java","-jar","/project_kv_061-1.0-jar-with-dependencies.jar"]


