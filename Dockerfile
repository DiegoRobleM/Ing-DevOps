# ---------------------------------------------------------------------------
# Etapa 1 "build": compila el jar con Maven Wrapper dentro del contenedor.
# Asi "docker build ." funciona solo, sin depender de un build previo local.
# ---------------------------------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy AS build

WORKDIR /app

# Copiamos primero solo lo necesario para resolver dependencias (cache de capas)
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B dependency:go-offline

# Ahora copiamos el codigo fuente y compilamos (tests ya corrieron en el pipeline)
COPY src/ src/
RUN ./mvnw -B clean package -DskipTests

# ---------------------------------------------------------------------------
# Etapa 2 "runtime": imagen final, liviana, sin herramientas de build.
# ---------------------------------------------------------------------------
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Usuario no-root: buena practica de seguridad para contenedores
RUN groupadd -r spring && useradd -r -g spring spring
USER spring:spring

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

# Nota: la verificacion de salud se hace desde fuera del contenedor
# (probes de Kubernetes en k8s/app/app-deployment.yaml, o healthcheck de
# docker-compose.yml), por eso no se agrega un HEALTHCHECK aqui: la imagen
# jre-jammy no trae wget/curl y no vale la pena instalarlos solo para esto.

ENTRYPOINT ["java","-jar","app.jar"]
