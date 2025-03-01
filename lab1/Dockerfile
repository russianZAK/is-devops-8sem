# Использование базового образа Alpine
FROM alpine:latest

# Устанавка nginx и создание необходимых директорий
# --no-cache: не сохранять кеш пакетов
# Создание системных директорий, необходимых для работы nginx:
# - /run/nginx: PID-файлы и socket-файлы
# - /var/log/nginx: логи сервера
# - /var/lib/nginx: кеш и временные файлы
RUN apk add --no-cache nginx && \
    mkdir -p /run/nginx && \
    mkdir -p /var/log/nginx && \
    mkdir -p /var/lib/nginx

# Создание непривилегированного пользователя и группы для безопасности
# - -S: создание системного пользователя и группы (без домашней директории)
RUN addgroup -S webgroup && adduser -S web -G webgroup

# Изменение владельца системных директорий на созданного пользователя
RUN chown -R web:webgroup /var/lib/nginx /var/log/nginx /run/nginx

# Декларирование используемого порта
EXPOSE 80

# Переключение на непривилегированного пользователя перед запуском
USER web

# Запуск nginx в основном процесс контейнера
# -g "daemon off;": отключение демонизации nginx
ENTRYPOINT ["nginx"]
CMD ["-g", "daemon off;"]