FROM nginx:stable
COPY ./assets/nginx/nginx.http.config /etc/nginx/nginx.conf