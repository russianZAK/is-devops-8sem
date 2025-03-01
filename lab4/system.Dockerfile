# Использование базового образа Alpine
FROM alpine:latest

# Устанавка maven для сборки, затем очищается кеш
RUN apk add --no-cache maven