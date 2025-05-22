# SFast-Xpress

## Estructura del Proyecto

### Módulos F_BY_F (File By File)
El proyecto utiliza un patrón de diseño "File By File" para manejar procesos complejos que requieren un procesamiento secuencial. Este patrón se implementa en tres módulos principales:

- **f_by_f_cierre_agencia**: Maneja el proceso de cierre de agencia paso a paso.
- **f_by_f_dashboard_agencia**: Construye el dashboard de agencia de manera progresiva.
- **f_by_f_dashboard_gerencia**: Construye el dashboard de gerencia de manera progresiva.

Este patrón es especialmente útil cuando:
- Se necesita mantener un estado entre diferentes pasos de procesamiento
- El proceso es complejo y necesita ser dividido en pasos manejables
- Se requiere trazabilidad y control sobre cada paso
- Es necesario poder retomar el proceso desde cualquier punto en caso de fallos

## CI/CD Pipeline

El proyecto utiliza GitHub Actions para automatizar el proceso de integración y despliegue continuo.

### Workflow (.github/workflows/maven.yml)

#### Triggers
El pipeline se activa en:
- Push a la rama `main`
- Pull requests hacia la rama `main`

#### Jobs

##### CI (Integración Continua)
```yaml
runs-on: self-hosted
steps:
  - Checkout del código
  - Compilación y empaquetado con Maven
```

##### CD (Entrega Continua)
```yaml
needs: [CI]
runs-on: self-hosted
steps:
  - Conexión SSH al servidor
  - Detención del servicio anterior (puerto 8080)
  - Inicio de la aplicación con Spring Boot
```

### Proceso de Despliegue
1. El código se compila y empaqueta usando Maven
2. Se verifica que la compilación sea exitosa
3. Se conecta al servidor mediante SSH
4. Se detiene cualquier instancia previa de la aplicación
5. Se inicia la nueva versión usando Spring Boot

### Configuración Requerida
El pipeline requiere los siguientes secretos configurados en GitHub:
- `HOST`: Dirección del servidor
- `USERNAME`: Usuario SSH
- `KEY`: Clave SSH
- `PORT`: Puerto SSH