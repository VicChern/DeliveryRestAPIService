#install maven
FROM maven:3.6.1 AS build

#install node js
RUN curl -sL https://deb.nodesource.com/setup_10.x | bash - \
  && apt-get install -y nodejs

#install angular
RUN npm install -g @angular/cli

#owner of the current image
MAINTAINER Kv-061.java

#switch to our package dir
WORKDIR /project_kv_061

#Download all dependencies from our pom.xml file and making package from them
COPY . .
RUN mvn -f /project_kv_061/pom.xml clean package

#install light weight jre just for start our .jar file
FROM openjdk:11-jre

#copy .jar from target and start project_kv_061-1.0-jar-with-dependencies.jar
COPY --from=build /project_kv_061/target/project_kv_061-1.0-jar-with-dependencies.jar /project_kv_061-1.0-jar-with-dependencies.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/project_kv_061-1.0-jar-with-dependencies.jar"]

