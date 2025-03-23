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