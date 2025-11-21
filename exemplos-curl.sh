#!/bin/bash

# Script de testes para API de Análise de Humor
# Execute: chmod +x exemplos-curl.sh && ./exemplos-curl.sh

echo "🚀 Iniciando testes da API de Análise de Humor"
echo ""

# Variáveis
BASE_URL="http://localhost:8080"
EMAIL="teste@lyra.com"
PASSWORD="senha123"

echo "📝 Passo 1: Registrando usuário..."
SIGNUP_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/auth/signup \
  -H "Content-Type: application/json" \
  -d "{
    \"firstName\": \"Teste\",
    \"lastName\": \"Lyra\",
    \"email\": \"${EMAIL}\",
    \"password\": \"${PASSWORD}\",
    \"roles\": [\"user\"]
  }")

echo "Resposta: $SIGNUP_RESPONSE"
echo ""

echo "🔐 Passo 2: Fazendo login..."
LOGIN_RESPONSE=$(curl -s -X POST ${BASE_URL}/api/auth/signin \
  -H "Content-Type: application/json" \
  -d "{
    \"email\": \"${EMAIL}\",
    \"password\": \"${PASSWORD}\"
  }")

# Extrai o token (requer jq instalado)
if command -v jq &> /dev/null; then
    TOKEN=$(echo $LOGIN_RESPONSE | jq -r '.token')
    echo "Token obtido: ${TOKEN:0:50}..."
else
    echo "Resposta: $LOGIN_RESPONSE"
    echo ""
    echo "⚠️  Instale 'jq' para extrair o token automaticamente: brew install jq"
    echo "Por favor, copie o token manualmente e execute os próximos comandos"
    exit 1
fi

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "🧪 Passo 3: Testando Nível 0 (Leve)..."
echo ""
curl -X POST ${BASE_URL}/api/humor/analisar \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "OTIMO",
    "descricao": "Hoje foi um dia muito produtivo no trabalho. Consegui finalizar todas as minhas tarefas e ainda ajudei um colega."
  }' | jq '.'

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "🧪 Passo 4: Testando Nível 1 (Moderado)..."
echo ""
curl -X POST ${BASE_URL}/api/humor/analisar \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "NEUTRO",
    "descricao": "Hoje tive uma discussão com o meu chefe sobre prazos. Me sinto um pouco frustrado, mas sei que vai passar."
  }' | jq '.'

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "🧪 Passo 5: Testando Nível 2 (Grave)..."
echo ""
curl -X POST ${BASE_URL}/api/humor/analisar \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "NAO_TAO_BEM",
    "descricao": "Estou me sentindo muito sobrecarregado. Não consigo dormir direito há dias, estou com muita ansiedade e não sei como lidar com tudo isso."
  }' | jq '.'

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""

echo "🚨 Passo 6: Testando Nível 3 (Gravíssimo - EMERGÊNCIA)..."
echo ""
curl -X POST ${BASE_URL}/api/humor/analisar \
  -H "Authorization: Bearer ${TOKEN}" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "DIFICIL",
    "descricao": "Não aguento mais viver assim. Sinto que não há saída e que nada vai melhorar. Não vejo sentido em continuar."
  }' | jq '.'

echo ""
echo "━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━"
echo ""
echo "✅ Testes concluídos!"
echo ""
