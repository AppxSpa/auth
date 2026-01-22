sudo docker pull mirkogutierrezappx/auth:latest

sudo docker stop auth-container 2>/dev/null
sudo docker rm auth-container 2>/dev/null


sudo docker run \
           --restart always \
           -d -p 8083:8083 \
           --env-file .env \
           --network appx \
           --add-host=host.docker.internal:host-gateway \
           --name auth-container auth \
           mirkogutierrezappx/auth:latest

