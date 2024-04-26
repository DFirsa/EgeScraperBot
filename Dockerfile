#=================#
#   BUILD STAGE   #
#=================#
FROM gradle:jdk21-alpine as build
ENV APP_HOME=/app
WORKDIR $APP_HOME
COPY --chown=gradle:gradle build.gradle.kts settings.gradle.kts gradle.properties $APP_HOME/
COPY --chown=gradle:gradle src $APP_HOME/src

ARG jdbc
ARG pwd
ARG usr
RUN gradle clean bootJar --no-daemon -x test -x generateJooq

#=================#
#  PACKAGE STAGE  #
##=================#
FROM openjdk:21
ENV JAR_PATH=/app/build/libs/app.jar
COPY --from=build $JAR_PATH app.jar
ENTRYPOINT java -jar app.jar
EXPOSE 8080