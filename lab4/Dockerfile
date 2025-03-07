# Использование базового образа Alpine
FROM alpine:latest as system

# Устанавка maven для сборки, затем очищается кеш
RUN apk add --no-cache maven



# Использование system.Dockerfile как базовый образ для этапа сборки
FROM system AS build

# Устанавка рабочей директории
WORKDIR /app

# Копирование файла pom.xml и загрузка зависимостей
COPY pom.xml .

# --go-offline: кэширование зависимостей для автономной сборки
# -B: режим без интерактивного вывода (batch mode)
RUN mvn dependency:go-offline -B

# Копирование исходного кода и сборка проекта
COPY src src
RUN mvn package

# Создание директории для сохранения финального JAR файла и копирование его туда
RUN mkdir /output && cp target/*.jar /output/



# Использование базового образа Alpine для создания финального образа для выполнения приложения
FROM alpine:latest AS runtime

# Установка JRE для запуска Java приложений
RUN apk add --no-cache openjdk17-jre

# Создание системного пользователя для безопасности:
# - -S: создание пользователя без домашней директории
# - -G appgroup: добавление пользователя в группу
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Использование переменных окружения для конфигурации
ENV APP_HOME="/app" \
    JAVA_OPTS=""

# Устанавка рабочей директории
WORKDIR $APP_HOME

# Копирование собранного JAR из этапа сборки
COPY --from=build /output/*.jar app.jar

# Изменение владельца файла на непривилегированного пользователя
RUN chown -R appuser:appgroup $APP_HOME

# Переключение на непривилегированного пользователя перед запуском
USER appuser

# Декларирование используемого порта
EXPOSE 8080

# Запуск приложения
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]