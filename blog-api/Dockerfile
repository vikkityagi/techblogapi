FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY target/blog-api-0.0.1-SNAPSHOT.jar blog-api-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java", "-jar", "blog-api-0.0.1-SNAPSHOT.jar"]
