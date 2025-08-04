#!/bin/bash

# Prepare Jar
mvn clean
mvn package

#Ensure, that docker-compose stopped
docker-compose stop

# Add environment variables
export BOT_NAME='Job_Connect_Bot'
export BOT_TOKEN='5698114090:AAE_OzmFDzFCf4ugCQbWUbuWAPcLQyHb9jc'
export BOT_DB_USERNAME='job_connect_user'
export BOT_DB_PASSWORD='YPHYkpdm?'

# Start new deployment
docker-compose up --build -d