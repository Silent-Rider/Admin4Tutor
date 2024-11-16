FROM openjdk:21-jdk-slim
RUN apt-get update && apt-get install -y ca-certificates ca-certificates-java
RUN update-ca-certificates -f && apt-get install -y maven
WORKDIR /app
COPY pom.xml /app
RUN mvn dependency:go-offline -B
COPY src /app/src
RUN mvn clean package -DskipTests
ENTRYPOINT [ "java", "-jar", "target/server-1.0.0.jar" ]