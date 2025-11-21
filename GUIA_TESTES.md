# 🧪 Guia Completo de Testes - Sistema de Análise de Humor

## 📋 Pré-requisitos

1. ✅ RabbitMQ rodando (porta 5672)
2. ✅ Sistema .NET rodando (porta 5000) - opcional para testes completos
3. ✅ Java 17+ instalado
4. ✅ Maven instalado

---

## 🚀 Passo 1: Iniciar a Aplicação

### Compilar o projeto
```bash
cd /Users/ucarols/LyraJava
mvn clean install
```

### Executar a aplicação
```bash
mvn spring-boot:run
```

### Verificar se está rodando
```bash
# A aplicação deve estar em:
http://localhost:8080
```

---

## 🔐 Passo 2: Obter Token JWT

### 2.1 Registrar um usuário (se ainda não tiver)

**Endpoint:** `POST http://localhost:8080/api/auth/signup`

**Body (JSON):**
```json
{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@teste.com",
  "password": "senha123",
  "roles": ["user"]
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "João",
    "lastName": "Silva",
    "email": "joao@teste.com",
    "password": "senha123",
    "roles": ["user"]
  }'
```

**Resposta esperada:**
```json
{
  "message": "Usuário registrado com sucesso!"
}
```

---

### 2.2 Fazer Login

**Endpoint:** `POST http://localhost:8080/api/auth/signin`

**Body (JSON):**
```json
{
  "email": "joao@teste.com",
  "password": "senha123"
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{
    "email": "joao@teste.com",
    "password": "senha123"
  }'
```

**Resposta esperada:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "joao@teste.com",
  "roles": ["ROLE_USER"]
}
```

**⚠️ IMPORTANTE:** Copie o valor do campo `token` - você vai usar em todos os próximos testes!

---

## 🧠 Passo 3: Testar Análise de Humor

### 3.1 Teste Nível 0 (Leve)

**Endpoint:** `POST http://localhost:8080/api/humor/analisar`

**Headers:**
```
Authorization: Bearer SEU_TOKEN_AQUI
Content-Type: application/json
```

**Body:**
```json
{
  "nivelHumorOriginal": "OTIMO",
  "descricao": "Hoje foi um dia muito produtivo no trabalho. Consegui finalizar todas as minhas tarefas e ainda ajudei um colega."
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/humor/analisar \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "OTIMO",
    "descricao": "Hoje foi um dia muito produtivo no trabalho. Consegui finalizar todas as minhas tarefas e ainda ajudei um colega."
  }'
```

**Resposta esperada (Nível 0 - Leve):**
```json
{
  "resumo": "Dia produtivo no trabalho com sensação de realização",
  "nivelGravidade": 0,
  "mensagem": "Mensagem do sistema .NET",
  "orientacao": "Orientações do sistema .NET",
  "requerAtencaoImediata": false,
  "fonte": "SISTEMA_NET"
}
```

---

### 3.2 Teste Nível 1 (Moderado)

**Body:**
```json
{
  "nivelHumorOriginal": "NEUTRO",
  "descricao": "Hoje tive uma discussão com o meu chefe sobre prazos. Me sinto um pouco frustrado, mas sei que vai passar."
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/humor/analisar \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "NEUTRO",
    "descricao": "Hoje tive uma discussão com o meu chefe sobre prazos. Me sinto um pouco frustrado, mas sei que vai passar."
  }'
```

**Resposta esperada (Nível 1 - Moderado):**
```json
{
  "resumo": "Conflito no trabalho causando frustração temporária",
  "nivelGravidade": 1,
  "mensagem": "Mensagem do sistema .NET",
  "orientacao": "Orientações do sistema .NET",
  "requerAtencaoImediata": false,
  "fonte": "SISTEMA_NET"
}
```

---

### 3.3 Teste Nível 2 (Grave)

**Body:**
```json
{
  "nivelHumorOriginal": "NAO_TAO_BEM",
  "descricao": "Estou me sentindo muito sobrecarregado. Não consigo dormir direito há dias, estou com muita ansiedade e não sei como lidar com tudo isso."
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/humor/analisar \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "NAO_TAO_BEM",
    "descricao": "Estou me sentindo muito sobrecarregado. Não consigo dormir direito há dias, estou com muita ansiedade e não sei como lidar com tudo isso."
  }'
```

**Resposta esperada (Nível 2 - Grave com PRIORIDADE):**
```json
{
  "resumo": "Sobrecarga emocional com sintomas de ansiedade",
  "nivelGravidade": 2,
  "mensagem": "Mensagem do sistema .NET (PRIORIDADE)",
  "orientacao": "Orientações do sistema .NET",
  "requerAtencaoImediata": true,
  "fonte": "SISTEMA_NET"
}
```

---

### 3.4 Teste Nível 3 (Gravíssimo - EMERGÊNCIA)

**Body:**
```json
{
  "nivelHumorOriginal": "DIFICIL",
  "descricao": "Não aguento mais viver assim. Sinto que não há saída e que nada vai melhorar. Não vejo sentido em continuar."
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/humor/analisar \
  -H "Authorization: Bearer SEU_TOKEN_AQUI" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "DIFICIL",
    "descricao": "Não aguento mais viver assim. Sinto que não há saída e que nada vai melhorar. Não vejo sentido em continuar."
  }'
```

**Resposta esperada (Nível 3 - NÃO envia para .NET):**
```json
{
  "resumo": "Sinais de risco extremo identificados",
  "nivelGravidade": 3,
  "mensagem": "Percebemos que você está passando por um momento muito difícil...\n\n📞 CVV - Centro de Valorização da Vida\nLigue: 188 (24h)...",
  "orientacao": "AÇÃO IMEDIATA NECESSÁRIA:\n1. Ligue para o CVV (188) AGORA...",
  "requerAtencaoImediata": true,
  "fonte": "SISTEMA_JAVA"
}
```

---

## 🧪 Passo 4: Testes com Postman

### 4.1 Importar Collection

Crie uma collection no Postman com as seguintes requisições:

**1. Signup**
- Method: POST
- URL: `http://localhost:8080/api/auth/signup`
- Body: raw JSON (exemplo acima)

**2. Login**
- Method: POST
- URL: `http://localhost:8080/api/auth/signin`
- Body: raw JSON (exemplo acima)
- Tests (para salvar token automaticamente):
```javascript
var jsonData = pm.response.json();
pm.environment.set("jwt_token", jsonData.token);
```

**3. Análise de Humor**
- Method: POST
- URL: `http://localhost:8080/api/humor/analisar`
- Headers: 
  - `Authorization: Bearer {{jwt_token}}`
  - `Content-Type: application/json`
- Body: raw JSON (exemplos acima)

---

## 📊 Passo 5: Verificar Logs

Enquanto testa, observe os logs da aplicação:

### Logs esperados para análise bem-sucedida:

```
INFO  - Iniciando análise de humor - Nível original: TRES
INFO  - Enviando análise de humor para Gemini AI
INFO  - Resposta recebida do Gemini
INFO  - JSON extraído: {"ResumoRecebido": "...", "Nivel": 1}
INFO  - Análise do Gemini concluída - Nível: 1, Resumo: ...
INFO  - Nível 1 detectado - Enviando para sistema .NET
INFO  - Enviando análise para sistema .NET - Nível: 1, Prioridade: false
INFO  - Resposta recebida do sistema .NET com sucesso
```

### Logs para nível gravíssimo:

```
INFO  - Iniciando análise de humor - Nível original: UM
INFO  - Enviando análise de humor para Gemini AI
INFO  - Resposta recebida do Gemini
ERROR - Nível GRAVÍSSIMO detectado - Gerando resposta de emergência
```

---

## 🔍 Passo 6: Testar Validações

### 6.1 Descrição muito curta (deve falhar)

```json
{
  "nivelHumorOriginal": "TRES",
  "descricao": "Triste"
}
```

**Resposta esperada:**
```json
{
  "descricao": "A descrição deve ter entre 10 e 2000 caracteres"
}
```

### 6.2 Sem nível de humor (deve falhar)

```json
{
  "descricao": "Estou me sentindo muito mal hoje"
}
```

**Resposta esperada:**
```json
{
  "nivelHumorOriginal": "O nível de humor original é obrigatório"
}
```

### 6.3 Sem token (deve falhar)

Remova o header `Authorization` e tente fazer a requisição.

**Resposta esperada:**
```json
{
  "status": 401,
  "error": "Não autorizado"
}
```

---

## 🧪 Passo 7: Testar Integração com .NET (Opcional)

Se você tiver o sistema .NET rodando:

### 7.1 Verificar se .NET está acessível

```bash
curl http://localhost:5000/api/humor/analise
```

### 7.2 Configurar URL do .NET

No `application.properties`:
```properties
dotnet.api.url=http://localhost:5000
```

### 7.3 Testar fluxo completo

Use os testes de Nível 0, 1 ou 2 (que enviam para .NET).

---

## 📝 Checklist de Testes

- [ ] Aplicação inicia sem erros
- [ ] Consegue registrar novo usuário
- [ ] Consegue fazer login e receber token
- [ ] Token é aceito nas requisições autenticadas
- [ ] Análise Nível 0 funciona
- [ ] Análise Nível 1 funciona
- [ ] Análise Nível 2 funciona (com prioridade)
- [ ] Análise Nível 3 funciona (mensagem de emergência)
- [ ] Validação de campos funciona
- [ ] Logs estão sendo gerados corretamente
- [ ] Integração com Gemini funciona
- [ ] Integração com .NET funciona (se disponível)

---

## 🚨 Troubleshooting

### Erro: "Unauthorized"
- Verifique se o token está correto
- Verifique se o header `Authorization: Bearer TOKEN` está presente
- Faça login novamente para obter novo token

### Erro: "Erro ao comunicar com Gemini AI"
- Verifique se a API Key está correta no `application.properties`
- Verifique sua conexão com internet
- Verifique se a URL do Gemini está correta

### Erro: "Erro ao comunicar com sistema .NET"
- Verifique se o sistema .NET está rodando
- Verifique a URL configurada em `dotnet.api.url`
- Para testes sem .NET, o sistema retorna mensagem padrão

### Erro: RabbitMQ connection refused
- Inicie o RabbitMQ: `brew services start rabbitmq` (Mac)
- Ou: `sudo systemctl start rabbitmq-server` (Linux)
- Ou: Inicie o serviço do RabbitMQ (Windows)

---

## 📱 Exemplo Completo de Teste

```bash
# 1. Registrar
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"firstName":"Test","lastName":"User","email":"test@test.com","password":"test123","roles":["user"]}'

# 2. Login (copie o token da resposta)
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"test@test.com","password":"test123"}'

# 3. Analisar humor (substitua SEU_TOKEN)
curl -X POST http://localhost:8080/api/humor/analisar \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "TRES",
    "descricao": "Hoje tive uma discussão com meu chefe e estou me sentindo frustrado"
  }'
```

---

## ✅ Teste Bem-Sucedido

Se tudo estiver funcionando, você verá:

1. ✅ Resposta JSON válida
2. ✅ Campo `fonte` indicando origem (SISTEMA_NET ou SISTEMA_JAVA)
3. ✅ Nível de gravidade correto (0-3)
4. ✅ Mensagem apropriada ao nível
5. ✅ Logs detalhados no console

---

**Bons testes! 🚀**
