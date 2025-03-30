FROM alpine:latest as nginx

RUN apk add --no-cache nginx && \
    mkdir -p /run/nginx && \
    mkdir -p /var/log/nginx && \
    mkdir -p /var/lib/nginx

RUN addgroup -S webgroup && adduser -S web -G webgroup

RUN chown -R web:webgroup /var/lib/nginx /var/log/nginx /run/nginx

COPY --from=frontend /app/build /usr/share/nginx/html

EXPOSE 80

USER web

ENTRYPOINT ["nginx"]
CMD ["-g", "daemon off;"]