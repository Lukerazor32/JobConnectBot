FROM adoptopenjdk/openjdk11:ubi
ARG JAR_FILE=target/*.jar
ENV BOT_NAME=Job_Connect_Bot
ENV BOT_TOKEN=5698114090:AAE_OzmFDzFCf4ugCQbWUbuWAPcLQyHb9jc
ENV BOT_DB_USERNAME=db_username
ENV BOT_DB_PASSWORD=db_password
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-Dbot.username=${BOT_NAME}", "-Dbot.token=${BOT_TOKEN}", "-Dspring.datasource.username=${BOT_DB_USERNAME}", "-Dspring.datasource.password=${BOT_DB_PASSWORD}", "-jar", "/app.jar"]