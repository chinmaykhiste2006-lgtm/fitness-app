#!/usr/bin/env bash
set -euo pipefail

DOCKER_USER="chinu12345"
PLATFORM="linux/amd64"

build_and_push() {
  SERVICE_DIR="$1"
  IMAGE_NAME="$2"

  echo "======================================"
  echo "Building and pushing $IMAGE_NAME"
  echo "======================================"

  docker buildx build \
    --platform "$PLATFORM" \
    --tag "$DOCKER_USER/$IMAGE_NAME:latest" \
    --push \
    "./$SERVICE_DIR"
}

build_and_push eureka fitness-eureka
build_and_push configserver fitness-config
build_and_push userservice fitness-user
build_and_push emailservice fitness-email
build_and_push aiservice fitness-ai
build_and_push activityservice fitness-activity
build_and_push gateway fitness-gateway

echo "All seven microservice images have been pushed."

