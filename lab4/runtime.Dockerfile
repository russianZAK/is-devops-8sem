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