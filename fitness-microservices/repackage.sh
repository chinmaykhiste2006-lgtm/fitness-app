#!/usr/bin/env bash
set -e

for svc in eureka configserver userservice emailservice aiservice activityservice gateway; do
  echo "=== Building $svc ==="
  (cd "$svc" && ./mvnw clean package -DskipTests)
done

echo "All JARs built."