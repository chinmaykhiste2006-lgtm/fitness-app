
#!/usr/bin/env bash
set -e

DOCKER_USER="chinu12345"

echo "======================================"
echo "Building Fitness Microservices Images"
echo "======================================"

build_service() {
  SERVICE_DIR="$1"
  IMAGE_NAME="$2"

  echo "=== Building ${SERVICE_DIR} ==="

  docker build \
    -t "${DOCKER_USER}/${IMAGE_NAME}:latest" \
    "./${SERVICE_DIR}"
}

build_service "eureka" "fitness-eureka"
build_service "configserver" "fitness-config"
build_service "userservice" "fitness-user"
build_service "emailservice" "fitness-email"
build_service "aiservice" "fitness-ai"
build_service "activityservice" "fitness-activity"
build_service "gateway" "fitness-gateway"

echo "======================================"
echo "Building Frontend Image"
echo "======================================"

docker build \
  -t "${DOCKER_USER}/fitness-frontend:latest" \
  "../fitness-frontend"

echo "======================================"
echo "All Docker images built successfully!"
echo "======================================"

docker images | grep "${DOCKER_USER}/fitness-"

