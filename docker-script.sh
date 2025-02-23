

# Create volumes for persistent stores
# Used to create a persistent volume that will preserve the
docker volume create oc-postgres
docker volume create oc-redis

docker run --init -d --name orkes-conductor -p 8070:8080 -p 1234:5000 --mount source=oc-redis,target=/redis --mount source=oc-postgres,target=/pgdata orkesio/orkes-conductor-community-standalone:latest