#!/bin/bash

# Si ejecutas: ./deploy.sh prod -> descarga de la nube
# Si ejecutas: ./deploy.sh      -> compila localmente (Modo Desarrollo)

MODO=${1:-"dev"}
NOMBRE_IMAGEN="mirkogutierrezappx/auth"

if [ "$MODO" == "prod" ]; then
    echo "--- MODO PRODUCCIÓN: Bajando imagen de Docker Hub ---"
    docker pull $NOMBRE_IMAGEN:latest
else
    echo "--- MODO DESARROLLO: Compilando localmente ---"
    # Compila el JAR saltando los tests para ir más rápido
    ./mvnw clean package -DskipTests
    # Construye la imagen local con el mismo nombre
    docker build -t $NOMBRE_IMAGEN:latest .
fi

echo "--- Limpiando contenedor anterior ---"
docker stop auth-container 2>/dev/null
docker rm auth-container 2>/dev/null

echo "--- Iniciando contenedor ---"
docker run \
           --restart always \
           -d -p 8083:8083 \
           --env-file .env \
           --network appx \
           --add-host=host.docker.internal:host-gateway \
           --name auth-container \
           $NOMBRE_IMAGEN:latest

# Limpieza de imágenes huérfanas
docker image prune -f

echo "--- ¡Listo! Accede a: http://localhost:8083 ---"