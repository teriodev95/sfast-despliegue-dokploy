# Dockerfile multi-stage para Spring Boot - Conecta a BD externas
# Etapa 1: Build con Maven
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build

WORKDIR /app

# Copiar archivos de configuración de Maven primero (cache de dependencias)
COPY pom.xml .

# Descargar dependencias (esta capa se cachea si pom.xml no cambia)
RUN mvn dependency:go-offline -B || true

# Copiar el código fuente
COPY src ./src

# Compilar la aplicación (omitir tests en build de producción)
RUN mvn clean package -DskipTests

# Etapa 2: Runtime con JRE
FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# Crear usuario no-root para seguridad
RUN addgroup -S spring && adduser -S spring -G spring

# Crear directorio para PDFs y logs con permisos correctos
RUN mkdir -p /app/cierres_semanales /app/logs && \
    chown -R spring:spring /app

# Copiar el JAR desde la etapa de build
COPY --from=build /app/target/*.jar app.jar

# Cambiar a usuario no-root
USER spring:spring

# Variables de entorno por defecto (se pueden sobrescribir en Dokploy)
ENV SPRING_PROFILES_ACTIVE=prod \
    JAVA_OPTS="-Xms256m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200" \
    SERVER_PORT=8080

# Exponer puerto de la aplicación
EXPOSE 8080

# Health check usando el actuator de Spring Boot
HEALTHCHECK --interval=30s --timeout=10s --start-period=90s --retries=3 \
  CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1

# Ejecutar la aplicación
ENTRYPOINT ["sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar app.jar"]
