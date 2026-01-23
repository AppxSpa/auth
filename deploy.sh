#!/bin/bash

# Uso:
# ./deploy.sh        -> Modo Desarrollo (Compila y corre local en tu laptop)
# ./deploy.sh prod   -> Modo Producción (Baja la imagen de GitHub/Docker Hub)

OPCION=${1:-"dev"}
NOMBRE_IMAGEN="mirkogutierrezappx/auth"

if [ "$OPCION" == "prod" ]; then
    echo "--- MODO PRODUCCIÓN: Bajando imagen oficial de Docker Hub ---"
    docker pull $NOMBRE_IMAGEN:latest
else
    echo "--- MODO DESARROLLO: Compilando localmente en Laptop ---"
    ./mvnw clean package -DskipTests
    docker build -t $NOMBRE_IMAGEN:latest .
fi

echo "--- Reiniciando Contenedor ---"
docker stop auth-container 2>/dev/null
docker rm auth-container 2>/dev/null

docker run \
           --restart always \
           -d -p 8083:8083 \
           --env-file .env \
           --network appx \
           --add-host=host.docker.internal:host-gateway \
           --name auth-container \
           $NOMBRE_IMAGEN:latest

docker image prune -f
echo "--- Despliegue finalizado en modo: $OPCION ---"