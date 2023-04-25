FROM eclipse-temurin:19.0.2_7-jdk-alpine

RUN apk --no-cache add inotify-tools

WORKDIR /app

COPY .mvn ./.mvn

COPY mvnw ./

COPY pom.xml ./

RUN ./mvnw dependency:go-offline

COPY entrypoint.sh ./

COPY src ./src

CMD [ "sh","-c","./entrypoint.sh" ]