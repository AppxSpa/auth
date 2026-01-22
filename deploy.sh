#!/bin/bash

# 1. Bajar la última versión de Docker Hub
sudo docker pull mirkogutierrezappx/auth:latest

# 2. Limpieza de contenedores anteriores
sudo docker stop auth-container 2>/dev/null
sudo docker rm auth-container 2>/dev/null

# 3. Ejecución corregida
sudo docker run \
           --restart always \
           -d -p 8083:8083 \
           --env-file .env \
           --network appx \
           --add-host=host.docker.internal:host-gateway \
           --name auth-container \
           mirkogutierrezappx/auth:latest