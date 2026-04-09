FROM eclipse-temurin:21-jdk-alpine AS builder

RUN apk add --no-cache maven

WORKDIR /workspace

COPY pom.xml .
RUN mvn dependency:go-offline -q 2>/dev/null || true

COPY src ./src
RUN mvn clean package -DskipTests -q

RUN java -Djarmode=layertools -jar target/student-management-*.jar extract --destination /workspace/extracted

FROM eclipse-temurin:21-jre-alpine AS runtime

RUN addgroup -S smsgroup && adduser -S smsuser -G smsgroup
USER smsuser

WORKDIR /app

COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/dependencies          ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/spring-boot-loader    ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/snapshot-dependencies ./
COPY --from=builder --chown=smsuser:smsgroup /workspace/extracted/application           ./

EXPOSE 8080

ENTRYPOINT ["java", \
  "-XX:+UseContainerSupport", \
  "-XX:MaxRAMPercentage=75.0", \
  "-Dspring.profiles.active=${SPRING_PROFILES_ACTIVE:-prod}", \
  "org.springframework.boot.loader.launch.JarLauncher"]