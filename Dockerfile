# ============================================================
# Multi-stage Dockerfile for Student Management System
# Stage 1: Build  |  Stage 2: Runtime (JRE 21 slim)
# ============================================================

# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

# Cache Maven dependencies first
COPY pom.xml .
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f pom.xml dependency:go-offline -q 2>/dev/null || true

# Copy source and build
COPY src ./src
RUN --mount=type=cache,target=/root/.m2 \
    mvn -f pom.xml clean package -DskipTests -q

# Extract Spring Boot layers for efficient Docker caching
RUN java -Djarmode=layertools -jar target/student-management-*.jar extract --destination /workspace/extracted

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

# Security: run as non-root
RUN addgroup -S smsgroup && adduser -S smsuser -G smsgroup
USER smsuser

WORKDIR /app

# Copy layers in order of least → most frequently changed
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/dependencies          ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/spring-boot-loader    ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/snapshot-dependencies ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/application           ./

# Create logs directory
RUN mkdir -p /app/logs

EXPOSE 8080

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:prod}", \
  "org.springframework.boot.loader.launch.JarLauncher"]
