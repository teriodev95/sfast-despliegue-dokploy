#!/bin/bash

# Script para desplegar a dokploy excluyendo GitHub Actions
# Uso: ./deploy-dokploy.sh [mensaje-commit]

set -e

REMOTE_NAME="dokploy"
BRANCH_NAME="main"
TEMP_BRANCH="temp-dokploy-deploy"

# Mensaje de commit por defecto
COMMIT_MSG="${1:-Deploy to dokploy - $(date '+%Y-%m-%d %H:%M:%S')}"

echo "🚀 Iniciando despliegue a dokploy..."
echo "📝 Mensaje de commit: $COMMIT_MSG"

# Verificar que estamos en la rama main
CURRENT_BRANCH=$(git branch --show-current)
if [ "$CURRENT_BRANCH" != "main" ]; then
    echo "❌ Error: Debes estar en la rama main para hacer el despliegue"
    exit 1
fi

# Verificar que no hay cambios sin commitear
if ! git diff-index --quiet HEAD --; then
    echo "❌ Error: Hay cambios sin commitear. Haz commit primero."
    exit 1
fi

# Crear rama temporal
echo "🔄 Creando rama temporal..."
git checkout -b $TEMP_BRANCH

# Eliminar el directorio .github/workflows/
echo "🗑️  Eliminando directorio .github/workflows/..."
rm -rf .github/

# Hacer commit de los cambios
echo "💾 Haciendo commit de los cambios..."
git add -A
git commit -m "$COMMIT_MSG"

# Push al remote dokploy
echo "📤 Enviando cambios a dokploy..."
git push $REMOTE_NAME $TEMP_BRANCH:$BRANCH_NAME --force

# Volver a la rama main
echo "🔙 Volviendo a la rama main..."
git checkout main

# Eliminar rama temporal
echo "🧹 Limpiando rama temporal..."
git branch -D $TEMP_BRANCH

echo "✅ Despliegue completado exitosamente!"
echo "🌐 Los cambios han sido enviados a: $REMOTE_NAME/$BRANCH_NAME"
echo "📋 Directorio .github/workflows/ excluido del despliegue"
