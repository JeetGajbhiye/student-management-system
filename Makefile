# ============================================================
# Makefile  –  Student Management System
# Common developer shortcuts
# ============================================================

.PHONY: help build run test clean docker-build docker-up docker-down docker-logs

## Display this help message
help:
	@awk 'BEGIN {FS = ":.*##"; printf "\nUsage:\n  make \033[36m<target>\033[0m\n\nTargets:\n"} \
	/^[a-zA-Z_-]+:.*?##/ { printf "  \033[36m%-20s\033[0m %s\n", $$1, $$2 }' $(MAKEFILE_LIST)

## Build the project (skip tests)
build: ## Build JAR without running tests
	./mvnw clean package -DskipTests

## Run with dev profile (H2 in-memory)
run: ## Start app with dev profile
	./mvnw spring-boot:run -Dspring-boot.run.profiles=dev

## Run all unit tests
test: ## Run unit tests with coverage
	./mvnw test

## Run integration tests
test-it: ## Run integration tests
	./mvnw verify -P test

## Full clean
clean: ## Remove build artifacts
	./mvnw clean

## Generate JaCoCo coverage report
coverage: ## Generate HTML coverage report (target/site/jacoco)
	./mvnw test jacoco:report

## Build Docker image
docker-build: ## Build the Docker image
	docker build -t student-management-system:latest .

## Start all services with docker-compose
docker-up: ## Start app + MySQL + Adminer
	docker-compose up -d

## Start dev stack (H2 only)
docker-up-dev: ## Start dev stack
	docker-compose -f docker-compose-dev.yml up -d

## Stop all containers
docker-down: ## Stop and remove containers
	docker-compose down

## Follow application logs
docker-logs: ## Tail application container logs
	docker-compose logs -f sms-app

## Open Swagger UI in browser (macOS)
swagger: ## Open Swagger UI
	open http://localhost:8080/swagger-ui.html

## Open H2 console in browser (macOS)
h2: ## Open H2 console
	open http://localhost:8080/h2-console

## Print health check
health: ## Check application health
	curl -s http://localhost:8080/actuator/health | python3 -m json.tool
