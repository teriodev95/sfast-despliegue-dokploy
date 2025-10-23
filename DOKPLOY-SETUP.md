# Guía de Despliegue en Dokploy

Esta guía describe cómo desplegar la aplicación **SFast-Xpress** en Dokploy.

## Requisitos Previos

- Acceso a Dokploy
- Servidores externos de MariaDB y MongoDB ya configurados y funcionando
- Repositorio Git del proyecto accesible

## Arquitectura del Despliegue

La aplicación es un contenedor Docker independiente que se conecta a:
- **MariaDB**: Base de datos principal en `65.21.188.158:3306`
- **MongoDB**: Base de datos de reportes en MongoDB Atlas
- **Sentry**: Sistema de monitoreo de errores

## Pasos para Desplegar en Dokploy

### 1. Crear Nueva Aplicación en Dokploy

1. Accede a tu panel de Dokploy
2. Crea un nuevo proyecto o selecciona uno existente
3. Haz clic en "New Application"
4. Selecciona **"Docker"** como tipo de aplicación

### 2. Configurar el Repositorio

En la sección de configuración del repositorio:

- **Repository URL**: La URL de tu repositorio Git
- **Branch**: `main` (o la rama que desees desplegar)
- **Dockerfile Path**: `./Dockerfile`

### 3. Configurar Variables de Entorno

En la sección "Environment Variables", agrega las siguientes variables:

#### Variables Obligatorias:

```env
SPRING_PROFILES_ACTIVE=prod
```

#### Base de Datos MariaDB:
```env
DB_URL=jdbc:mariadb://65.21.188.158:3306/xpress_dinero
DB_USERNAME=xpress_admin
DB_PASSWORD=Xpr3ss@2024!Secure
```

#### MongoDB:
```env
MONGODB_URI=mongodb+srv://reports_xpress_user_rw:ULh0VykDvetWM3JM@cluster0.zoa5e.gcp.mongodb.net/ReportsXpress
```

#### Sentry (Monitoreo):
```env
SENTRY_DSN=https://91bb4322f0905e42c4acc5b9f3873975@o1037894.ingest.us.sentry.io/4508762622459904
SENTRY_TRACES_SAMPLE_RATE=1.0
```

#### Configuración de Java (Opcional):
```env
JAVA_OPTS=-Xms256m -Xmx1024m -XX:+UseG1GC -XX:MaxGCPauseMillis=200
```

### 4. Configurar el Puerto

- **Puerto de la Aplicación**: `8080`
- **Puerto Público**: El que prefieras (ej: `80`, `443`, o dejar que Dokploy asigne uno)

### 5. Configurar Health Check

Dokploy puede usar el health check del Dockerfile, o puedes configurar uno personalizado:

- **Health Check Path**: `/actuator/health`
- **Puerto**: `8080`
- **Intervalo**: `30s`
- **Timeout**: `10s`
- **Retries**: `3`

### 6. Configurar Volúmenes (Opcional pero Recomendado)

Para persistir logs y PDFs generados:

```yaml
/app/logs:/datos/sfast-xpress/logs
/app/cierres_semanales:/datos/sfast-xpress/pdfs
```

### 7. Configurar Recursos

Recomendaciones de recursos:

- **CPU**: 1-2 cores
- **Memoria**: 1GB - 2GB
- **Límite de Memoria**: 2GB

### 8. Deploy

1. Revisa toda la configuración
2. Haz clic en "Deploy" o "Create Application"
3. Dokploy comenzará a construir la imagen Docker
4. Una vez construida, iniciará el contenedor

## Verificación Post-Despliegue

### 1. Verificar Health Check

Accede a:
```
http://tu-dominio/actuator/health
```

Deberías ver una respuesta JSON con status `UP`.

### 2. Verificar Endpoints Disponibles

```
http://tu-dominio/actuator
```

Esto mostrará todos los endpoints de actuator disponibles.

### 3. Revisar Logs

En el panel de Dokploy, ve a la sección de logs para verificar que:
- La aplicación inició correctamente
- La conexión a MariaDB fue exitosa
- La conexión a MongoDB fue exitosa
- No hay errores en el inicio

### 4. Probar Conectividad a Bases de Datos

Verifica en los logs que aparezcan mensajes como:
```
HikariPool-1 - Start completed.
Connected to MongoDB successfully
```

## Endpoints Importantes

- **Health Check**: `/actuator/health`
- **Info**: `/actuator/info`
- **Metrics**: `/actuator/metrics`
- **Logs**: `/actuator/loggers`
- **HTTP Exchanges**: `/actuator/httpexchanges`

## Troubleshooting

### El contenedor no inicia

1. Revisa los logs en Dokploy
2. Verifica que todas las variables de entorno estén configuradas
3. Verifica la conectividad a las bases de datos externas

### Error de conexión a MariaDB

1. Verifica que el servidor MariaDB en `65.21.188.158:3306` sea accesible desde Dokploy
2. Verifica las credenciales
3. Asegúrate de que el firewall permita conexiones desde el servidor de Dokploy

### Error de conexión a MongoDB

1. Verifica que la URI de MongoDB Atlas sea correcta
2. Asegúrate de que la IP del servidor Dokploy esté en la lista blanca de MongoDB Atlas
3. Verifica las credenciales

### Alto uso de memoria

1. Ajusta `JAVA_OPTS` reduciendo `-Xmx1024m` a un valor menor
2. Incrementa los recursos asignados en Dokploy

### PDFs no se generan

1. Verifica que el volumen `/app/cierres_semanales` esté montado correctamente
2. Revisa los permisos del volumen en el host

## Actualización de la Aplicación

Para actualizar:

1. Haz push de los cambios a tu repositorio Git
2. En Dokploy, ve a tu aplicación
3. Haz clic en "Redeploy" o "Rebuild"
4. Dokploy reconstruirá la imagen y reiniciará el contenedor

## Rollback

Si algo falla:

1. Ve al historial de despliegues en Dokploy
2. Selecciona una versión anterior estable
3. Haz clic en "Rollback"

## Monitoreo Continuo

- **Sentry**: Monitoreo de errores en tiempo real
- **Spring Boot Actuator**: Métricas y salud de la aplicación
- **Logs de Dokploy**: Logs de la aplicación en tiempo real

## Notas de Seguridad

1. **Nunca expongas las credenciales** en el código fuente
2. Usa las variables de entorno de Dokploy para información sensible
3. Mantén actualizadas las dependencias de Maven
4. Revisa regularmente los logs de Sentry para detectar problemas

## Soporte

Para más información sobre:
- **Spring Boot**: https://spring.io/projects/spring-boot
- **Dokploy**: Consulta la documentación oficial de Dokploy
- **Esta aplicación**: Contacta al equipo de desarrollo
