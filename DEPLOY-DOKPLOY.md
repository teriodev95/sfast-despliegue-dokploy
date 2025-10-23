# 🚀 Despliegue a Dokploy

Este documento explica cómo desplegar el proyecto al repositorio de Dokploy excluyendo los GitHub Actions.

## 📋 Requisitos

- Estar en la rama `main`
- No tener cambios sin commitear
- Tener configurado el remote `dokploy`

## 🔧 Uso del Script de Despliegue

### Comando Básico
```bash
./deploy-dokploy.sh
```

### Con Mensaje Personalizado
```bash
./deploy-dokploy.sh "Fix: Corrección en el controlador de pagos"
```

## ⚙️ Qué Hace el Script

1. **Verificaciones previas:**
   - Confirma que estás en la rama `main`
   - Verifica que no hay cambios sin commitear

2. **Proceso de despliegue:**
   - Crea una rama temporal `temp-dokploy-deploy`
   - Elimina el directorio `.github/workflows/`
   - Hace commit de los cambios
   - Envía los cambios al remote `dokploy`
   - Limpia la rama temporal

3. **Resultado:**
   - El código se despliega sin los GitHub Actions
   - Tu repositorio local queda intacto
   - No afecta el repositorio principal

## 🔍 Configuración Actual

- **Remote principal:** `origin` → `https://github.com/Eleuterioxz/SFast-Xpress.git`
- **Remote dokploy:** `dokploy` → `https://github.com/teriodev95/sfast-despliegue-dokploy.git`

## 🚨 Notas Importantes

- El script usa `--force` para sobrescribir la rama en dokploy
- Los GitHub Actions se excluyen automáticamente
- El directorio `.github/workflows/` se mantiene en tu repositorio local
- Cada despliegue crea un commit específico para dokploy

## 🛠️ Troubleshooting

### Error: "Debes estar en la rama main"
```bash
git checkout main
```

### Error: "Hay cambios sin commitear"
```bash
git add .
git commit -m "Tu mensaje de commit"
```

### Verificar remotes configurados
```bash
git remote -v
```

## 📝 Ejemplo de Flujo de Trabajo

```bash
# 1. Hacer cambios en tu código
git add .
git commit -m "Feature: Nueva funcionalidad"

# 2. Push al repositorio principal
git push origin main

# 3. Desplegar a dokploy (sin GitHub Actions)
./deploy-dokploy.sh "Deploy: Nueva funcionalidad"
```
