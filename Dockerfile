FROM eclipse-temurin:21-jre

WORKDIR /app

COPY target/jobportal.jar jobportal.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","jobportal.jar"]
