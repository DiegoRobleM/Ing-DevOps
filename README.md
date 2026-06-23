# proyecto_springboot_usuarios

API REST de Usuarios (Spring Boot 3 + MySQL) con pipeline DevOps completo:
build y pruebas automatizadas, analisis de calidad (SonarCloud), imagen
Docker, despliegue en Kubernetes y monitoreo con Prometheus/Grafana.

La documentacion completa (arquitectura, explicacion paso a paso de cada
parte de la guia de evaluacion, y como reproducir todo) esta en
`docs/Documentacion_Solucion_DevOps.docx`.

## Estructura

```
src/                    Codigo de la app (controller, service, repository, model)
k8s/                    Manifiestos de Kubernetes (app, MySQL, Prometheus, Grafana, Pushgateway)
docs/                   Documentacion y dashboard de Grafana exportado
.github/workflows/      Pipeline de CI/CD (GitHub Actions)
Dockerfile              Build multi-stage de la imagen
docker-compose.yml      Para correr app + MySQL en local sin Kubernetes
```

## Correr en local (sin Kubernetes)

```bash
docker compose up -d --build
curl http://localhost:8080/usuarios
docker compose down -v   # limpiar
```

## Correr las pruebas

```bash
./mvnw clean verify
# Reporte de cobertura: target/site/jacoco/index.html
```

## Desplegar en Kubernetes (AWS Academy Learner Lab)

El cluster es un **k3s** corriendo en una instancia EC2 t3.small (2 GiB de
RAM, Ubuntu 24.04 LTS) de AWS Academy Learner Lab, con un runner self-hosted de
GitHub Actions instalado dentro de esa misma instancia. La imagen se
construye y publica en GHCR por el pipeline; el `kubectl apply` se ejecuta
directo en la EC2.

```bash
kubectl apply -f k8s/ --recursive

# URLs publicas una vez que los pods esten "Running"
# (Elastic IP asociada a la instancia EC2):
#   App:        http://52.54.36.238:30080/usuarios
#   Prometheus: http://52.54.36.238:30090
#   Grafana:    http://52.54.36.238:30030  (admin / admin)
#   Pushgateway:http://52.54.36.238:30091
```

```bash
kubectl get pods -n devops-usuarios -w   # ver el progreso del despliegue
kubectl logs deployment/springboot-app -n devops-usuarios -f   # logs de la app
```

El Security Group de la instancia EC2 debe tener abierto el rango de
NodePorts (30000-32767) ademas del puerto 22 para SSH.

## Pipeline CI/CD

Definido en `.github/workflows/ci-cd.yml`, 3 jobs:

1. **build-test-sonar** (GitHub-hosted): compila, corre pruebas con JaCoCo
   y analiza con SonarCloud. Si una prueba falla o el Quality Gate no pasa,
   el pipeline se detiene aqui.
2. **docker-build-push** (GitHub-hosted, solo push a `main`): construye y
   publica la imagen en GHCR (debe ser un paquete publico, o configurar un
   imagePullSecret en el cluster).
3. **deploy** (runner self-hosted instalado en la EC2 de AWS, solo push a
   `main`): aplica los manifiestos de `k8s/`, actualiza la imagen al tag
   del commit y publica metricas del pipeline (duracion, cobertura) en
   Pushgateway.

Configuracion necesaria antes de que el pipeline corra de punta a punta:
ver la seccion "Puesta en marcha" del documento Word en `docs/`.

## Licencia / contexto

Proyecto academico (evaluacion DevOps).
