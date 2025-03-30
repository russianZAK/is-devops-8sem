FROM alpine:latest as system

RUN apk add --no-cache maven

FROM system AS build

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline -B

COPY src src
RUN mvn package

RUN mkdir /output && cp target/*.jar /output/

FROM alpine:latest AS runtime

RUN apk add --no-cache openjdk17-jre

RUN addgroup -S appgroup && adduser -S appuser -G appgroup

ENV APP_HOME="/app"

WORKDIR $APP_HOME

COPY --from=build /output/*.jar app.jar

RUN chown -R appuser:appgroup $APP_HOME

USER appuser

CMD ["sh", "-c", "java -jar app.jar"]