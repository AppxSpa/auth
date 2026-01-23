#!/bin/bash

# 1. Bajar la última versión de Docker Hub
docker pull mirkogutierrezappx/auth:latest
# 2. Limpieza de contenedores anteriores
docker stop auth-container 2>/dev/null
docker rm auth-container 2>/dev/null

# 3. Ejecución corregida
docker run \
           --restart always \
           -d -p 8083:8083 \
           --env-file .env \
           --network appx \
           --add-host=host.docker.internal:host-gateway \
           --name auth-container \
           mirkogutierrezappx/auth:latest

# Borra imágenes antiguas que ya no se usan
docker image prune -f