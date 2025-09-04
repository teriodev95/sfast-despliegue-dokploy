# 🔍 Guía de Monitoreo de Logs - SFast-Xpress

## 🚀 URLs Base
- **Servidor:** https://sfast-api.terio.xyz
- **Actuator:** https://sfast-api.terio.xyz/actuator

## 📊 Endpoints Principales

### 1️⃣ **Ver Todos los Endpoints Disponibles**
```bash
curl https://sfast-api.terio.xyz/actuator
```

### 2️⃣ **Estado de Salud**
```bash
curl https://sfast-api.terio.xyz/actuator/health
```

### 3️⃣ **Ver Configuración de Logs**
```bash
curl https://sfast-api.terio.xyz/actuator/loggers
```

## 🔧 Configurar Logs para Endpoints Específicos

### **Habilitar DEBUG para endpoint de cobranza:**
```bash
# XpressController
curl -X POST https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers.XpressController \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'

# PGSController (PWA)
curl -X POST https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers.PGS.PGSController \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'
```

### **Habilitar DEBUG para todos los controladores:**
```bash
curl -X POST https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'
```

## 🧪 Probar Endpoints de Cobranza

### **Endpoint Principal (XpressController):**
```bash
curl "https://sfast-api.terio.xyz/xpress/v1/cobranza/GD043/2025/36"
```

### **Endpoint PWA (PGSController):**
```bash
curl "https://sfast-api.terio.xyz/xpress/v1/pwa/cobranza/GD043/2025/36"
```

## 📈 Monitoreo Avanzado

### **Ver logs en tiempo real:**
```bash
curl https://sfast-api.terio.xyz/actuator/logfile
```

### **Ver métricas:**
```bash
curl https://sfast-api.terio.xyz/actuator/metrics
```

### **Ver un logger específico:**
```bash
curl https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers.XpressController
```

## 🎯 Workflow de Debugging

1. **Habilitar DEBUG** en el controlador específico
2. **Ejecutar el endpoint** que quieres monitorear
3. **Consultar los logs** generados
4. **Revisar métricas** si es necesario
5. **Volver a INFO** cuando termines

### **Volver a nivel INFO:**
```bash
curl -X POST https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers.XpressController \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "INFO"}'
```

## 📝 Ejemplo Completo

```bash
# 1. Habilitar DEBUG
curl -X POST https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers.XpressController \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "DEBUG"}'

# 2. Probar endpoint
curl "https://sfast-api.terio.xyz/xpress/v1/cobranza/GD043/2025/36"

# 3. Ver logs
curl https://sfast-api.terio.xyz/actuator/logfile | tail -50

# 4. Volver a INFO
curl -X POST https://sfast-api.terio.xyz/actuator/loggers/tech.calaverita.sfast_xpress.controllers.XpressController \
  -H "Content-Type: application/json" \
  -d '{"configuredLevel": "INFO"}'
```

---
**Nota:** Los endpoints de Actuator están configurados SIN autenticación para facilitar el debugging. En producción final, se debe agregar seguridad.

