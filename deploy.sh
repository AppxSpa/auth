#!/bin/bash

# =========================================================
# CONFIGURACIÓN DEL MICROSERVICIO (Actualizado a GHCR)
# =========================================================
NOMBRE_APP="auth"
PUERTO="8083"

# CORRECCIÓN: Usar la organización AppxSpa en minúsculas
IMAGEN_HUB="ghcr.io/appxspa/auth" 
# =========================================================

OPCION=${1:-"dev"}

case $OPCION in
    "prod")
        echo "--- MODO PRODUCCIÓN: Bajando de GHCR ($IMAGEN_HUB) ---"
        docker pull $IMAGEN_HUB:latest
        TARGET_IMAGE="$IMAGEN_HUB:latest"
        ;;
    *)
        echo "--- MODO DESARROLLO: Compilando localmente ($NOMBRE_APP) ---"
        # Asegúrate de tener permisos en mvnw
        ./mvnw clean package -DskipTests
        docker build -t $NOMBRE_APP:local .
        TARGET_IMAGE="$NOMBRE_APP:local"
        ;;
esac

echo "--- Limpiando contenedor anterior ---"
docker stop ${NOMBRE_APP}-container 2>/dev/null
docker rm ${NOMBRE_APP}-container 2>/dev/null

echo "--- Iniciando contenedor en puerto $PUERTO ---"
# OJO: Verifica si la red es 'laflorida' o 'appx'
docker run \
           --restart always \
           -d \
           -p ${PUERTO}:${PUERTO} \
           --env-file .env \
           --network appx \
           --add-host=host.docker.internal:host-gateway \
           --name ${NOMBRE_APP}-container \
           $TARGET_IMAGE

docker image prune -f
echo "--- Proceso Terminado ($OPCION) ---"