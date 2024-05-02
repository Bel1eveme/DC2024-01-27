#!/bin/bash
# wait-for-it.sh
# Ожидание запуска указанного хоста и порта.

set -e

host="$1"
port="$2"
timeout="$3"

echo "Waiting for $host:$port..."

while ! nc -z "$host" "$port"; do
  if [ $timeout -gt 0 ]; then
    timeout=$((timeout - 1))
  else
    echo "Timeout exceeded"
    exit 1
  fi
  sleep 1
done

echo "Service $host:$port is available!"
