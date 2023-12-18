FROM openjdk:17-jdk-alpine

EXPOSE 8080

RUN mkdir /work

WORKDIR /work

COPY ./build/libs/weather_app-0.0.1-SNAPSHOT-plain.jar app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-Dspring.profiles.active=test", "-jar", "/work/app.jar"]