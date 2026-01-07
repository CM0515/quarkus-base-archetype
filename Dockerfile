## Build stage with native compilation
FROM quay.io/quarkus/ubi-quarkus-mandrel-builder-image:jdk-17 AS builder

# Install Maven
USER root
RUN microdnf install -y wget tar gzip && \
    wget https://archive.apache.org/dist/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz && \
    tar -xzf apache-maven-3.9.6-bin.tar.gz -C /opt && \
    ln -s /opt/apache-maven-3.9.6/bin/mvn /usr/local/bin/mvn && \
    rm apache-maven-3.9.6-bin.tar.gz && \
    microdnf clean all

USER quarkus
WORKDIR /project

# Copy Maven files for dependency caching
COPY --chown=quarkus:quarkus pom.xml .
RUN mvn dependency:go-offline -B

# Copy source and build
COPY --chown=quarkus:quarkus src ./src
RUN mvn clean package -Pnative -Dmaven.test.skip=true -B

## Final image
FROM quay.io/quarkus/quarkus-micro-image:2.0
WORKDIR /work/
COPY --from=builder /project/target/*-runner /work/application
RUN chmod 775 /work/application
EXPOSE 8080
CMD ["./application"]
