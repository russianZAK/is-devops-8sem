# Использование базового образа Alpine
FROM alpine:latest AS build

# Устанавка maven для сборки, затем очищается кеш
RUN apk add --no-cache maven

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

# Использование базового образа Alpine для создания финального образа для выполнения приложения
FROM alpine:latest

# Установка JRE для запуска Java приложений
RUN apk add --no-cache openjdk17-jre

# Использование переменных окружения для конфигурации
ENV APP_HOME="/app" \
    JAVA_OPTS=""

# Создание системного пользователя для безопасности:
# - -S: создание пользователя без домашней директории
# - -G appgroup: добавление пользователя в группу
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Устанавка рабочей директории
WORKDIR $APP_HOME

# Копирование собранного JAR из этапа сборки
COPY --from=build /app/target/*.jar app.jar

# Изменение владельца файла на непривилегированного пользователя
RUN chown -R appuser:appgroup $APP_HOME

# Переключение на непривилегированного пользователя перед запуском
USER appuser

# Декларирование используемого порта
EXPOSE 8080

# Запуск приложения
CMD ["sh", "-c", "java $JAVA_OPTS -jar app.jar"]
