# ---------- BUILD ----------
FROM maven:3.9.6-eclipse-temurin-17 AS builder
WORKDIR /project

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src
RUN mvn clean package -Dmaven.test.skip=true -B

# ---------- RUNTIME ----------
FROM eclipse-temurin:17-jre
WORKDIR /work

COPY --from=builder /project/target/quarkus-app/ /work/

EXPOSE 8080
CMD ["java", "-jar", "quarkus-run.jar"]
