# Variables de entorno dinamicas (.env.local por defecto)
ENV_FILE ?= .env.local

-include $(ENV_FILE)
export

# Detecta el OS
UNAME_S := $(shell uname -s 2>/dev/null || echo unknown)
ifeq ($(OS),Windows_NT)
    MVN_CMD := mvnw.cmd
else ifeq ($(UNAME_S),Linux)
    MVN_CMD := ./mvnw
else ifeq ($(UNAME_S),Darwin)
	MVN_CMD := ./mvnw
else
	MVN_CMD := ./mvnw
endif

# ----------- TARGETS -----------

.PHONY: help
help:
	@echo "\nComandos disponibles:"
	@echo "  make clean                 --> Limpia el target del proyecto"
	@echo "  make build                 --> Compila el proyecto (con tests)"
	@echo "  make test                  --> Ejecuta los tests"
	@echo "  make coverage              --> Genera el reporte Jacoco (cobertura)"
	@echo "  make deploy                --> Actualiza la libreria a Github Packages"
	@echo "  make sonar                 --> Ejecuta analisis SonarQube"
	@echo "  make sonar-up              --> Levanta SonarQube con Docker"
	@echo "  make sonar-down            --> Apaga SonarQube"
	@echo "  make db-up                 --> Levanta base de datos + Mongo + Kafka"
	@echo "  make db-down               --> Apaga base de datos + Mongo + Kafka"
	@echo "  make network-create        --> Crea red fly-net si no existe"
	@echo "  make logs                  --> Ver logs de todos los contenedores"
	@echo "  make mysql                 --> Acceder al cliente MySQL en el contenedor"
	@echo "  make mongo                 --> Acceder al shell de MongoDB"
	@echo "  make kafka-producer        --> Abrir consola producer Kafka"
	@echo "  make kafka-consumer        --> Abrir consola consumer Kafka"
	@echo "  make build-image			--> Buildea la imagen del microservicio"
	@echo "  make run-container			--> Ejecuta la imagen del microservicio"
	@echo "  make stop-container		--> Detiene y elimina el contenedor"

clean:
	$(MVN_CMD) clean

build:
	$(MVN_CMD) clean verify

test:
	$(MVN_CMD) test

coverage:
	$(MVN_CMD) clean verify

deploy:
	$(MVN_CMD) clean deploy

sonar:
	$(MVN_CMD) verify sonar:sonar \
	  -Dsonar.projectKey=$(SONAR_PROJECT_KEY) \
	  -Dsonar.projectName=$(SONAR_PROJECT_NAME) \
	  -Dsonar.projectVersion=$(SONAR_PROJECT_VERSION) \
	  -Dsonar.token=$(SONAR_TOKEN) \
	  -Dsonar.host.url=$(SONAR_HOST_URL) \
	  -Dsonar.sources=src/main/java \
	  -Dsonar.tests=src/test/java \
	  -Dsonar.java.binaries=target/classes \
	  -Dsonar.coverage.jacoco.xmlReportPaths=target/site/jacoco/jacoco.xml

VERSION := $(shell $(MVN_CMD) help:evaluate -Dexpression=project.version -q -DforceStdout)

sonar-up:
	docker compose -f Docker/docker-compose.sonar.yml --env-file $(ENV_FILE) --project-name flycommerce up -d

sonar-down:
	docker compose -f Docker/docker-compose.sonar.yml --env-file $(ENV_FILE) --project-name flycommerce down

db-up:
	docker compose -f Docker/docker-compose.yml --env-file $(ENV_FILE) --project-name flycommerce up -d

db-down:
	docker compose -f Docker/docker-compose.yml --env-file $(ENV_FILE) --project-name flycommerce down

network-create:
	docker network create fly-net || true

logs:
	docker compose -f Docker/docker-compose.yml --env-file $(ENV_FILE) --project-name flycommerce logs -f

mysql:
	docker exec -it dbfly mysql -u$(DB_USER) -p$(DB_PASS) $(DB_NAME)

mongo:
	docker exec -it mongofly mongosh

kafka-producer:
	docker exec -it kafkafly kafka-console-producer --broker-list localhost:9092 --topic test-topic

kafka-consumer:
	docker exec -it kafkafly kafka-console-consumer --bootstrap-server localhost:9092 --topic test-topic --from-beginning

build-image:
	docker build --build-arg SKIP_UNIT_TESTS=$(SKIP_UNIT_TESTS) --build-arg SPRING_PROFILE=$(SPRING_PROFILES_ACTIVE) -f Docker/Dockerfile -t flycommerce:$(VERSION) .

run-container:
	docker run --network fly-net --name flycommerce-container --env-file $(ENV_FILE) -p $(PORT):$(PORT) -d flycommerce:$(VERSION)

stop-container:
	docker rm -f flycommerce-container || true
.DEFAULT_GOAL := help