# ---------- Stage 1: Build ----------
FROM eclipse-temurin:21-jdk-alpine AS builder

WORKDIR /workspace

COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -q 2>/dev/null || true

COPY src ./src
RUN ./mvnw clean package -DskipTests -q

RUN java -Djarmode=layertools -jar target/student-management-*.jar extract --destination /workspace/extracted

# ---------- Stage 2: Runtime ----------
FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S smsgroup && adduser -S smsuser -G smsgroup
USER smsuser

WORKDIR /app

COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/dependencies          ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/spring-boot-loader    ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/snapshot-dependencies ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/application           ./

RUN mkdir -p /app/logs

EXPOSE 8080

HEALTHCHECK --interval=30s --timeout=10s --start-period=60s --retries=3 \
  CMD wget -qO- http://localhost:8080/actuator/health | grep -q '"status":"UP"' || exit 1

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-XX:+UseG1GC", \
  "-Djava.security.egd=file:/dev/./urandom", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}", \
  "org.springframework.boot.loader.launch.JarLauncher"]