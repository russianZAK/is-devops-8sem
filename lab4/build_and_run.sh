#!/bin/bash

echo "Сборка образа из system.Dockerfile..."
docker build -f system.Dockerfile -t system .

echo "Сборка образа из build.Dockerfile..."
docker build -f build.Dockerfile -t build .

echo "Запуск docker-compose..."
docker-compose up -d
