#!/bin/bash

# SwiftURL Docker Startup Script

echo "🚀 Starting SwiftURL Infrastructure..."

# Check if docker is running
if ! docker info >/dev/null 2>&1; then
    echo "❌ Error: Docker is not running. Please start Docker and try again."
    exit 1
fi

# Build and start the containers
echo "📦 Building and starting containers..."
docker-compose up --build -d

echo "---------------------------------------------------"
echo "✅ SwiftURL is starting up!"
echo "🌐 Web Interface: http://localhost:8080"
echo "📊 Database: localhost:5432"
echo "⚡ Redis: localhost:6379"
echo "---------------------------------------------------"
echo "📝 To view logs, run: docker-compose logs -f app"
echo "🛑 To stop the app, run: docker-compose down"

