FROM alpine:latest AS frontend_build

WORKDIR /app

RUN apk add --no-cache nodejs npm

COPY package*.json ./

RUN npm install

COPY . .

RUN npm run build

FROM alpine:latest as frontend

COPY --from=frontend_build /app/build /app/build