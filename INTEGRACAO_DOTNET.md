# 🔗 Integração com Sistema .NET

## 📍 Onde Está a Integração

### **Arquivo Principal:**
```
/src/main/java/com/example/lyra/service/DotNetIntegrationService.java
```

### **Configuração:**
```
/src/main/resources/application.properties
```

---

## ⚙️ Configuração Atual

### application.properties

```properties
# .NET API Integration
dotnet.api.url=http://localhost:5000
dotnet.api.timeout=10
```

**Endpoint .NET esperado:**
```
POST http://localhost:5000/api/humor/analise
```

---

## 🔄 Fluxo Completo

### **1. Requisição do Frontend**
```json
POST /api/humor/analisar
{
  "nivelHumorOriginal": "NAO_TAO_BEM",
  "descricao": "Estou com ansiedade"
}
```

### **2. Java Processa (HumorAnalysisService.java)**
```java
// Linha 27-33
public HumorAnalysisResponse processarAnaliseHumor(HumorAnalysisRequest request) {
    // Envia para Gemini AI
    GeminiAnalysisResponse geminiResponse = geminiAIService.analisarHumor(request.getDescricao());
    
    // Processa conforme nível (0-3)
    return processarPorNivelGravidade(geminiResponse, request.getDescricao());
}
```

### **3. Decisão por Nível**
```java
// Linha 52-73
if (nivel == 0 || nivel == 1) {
    // Envia para .NET (normal)
    return enviarParaDotNetENormal(resumo, nivel, descricaoOriginal, false);
}
else if (nivel == 2) {
    // Envia para .NET com PRIORIDADE
    return enviarParaDotNetENormal(resumo, nivel, descricaoOriginal, true);
}
else if (nivel == 3) {
    // NÃO envia para .NET - Gera resposta de emergência
    return gerarRespostaEmergencia(resumo, nivel);
}
```

### **4. Chamada HTTP para .NET (DotNetIntegrationService.java)**
```java
// Linha 40-52
DotNetHumorResponse response = webClient
    .post()
    .uri("/api/humor/analise")  // ← Endpoint .NET
    .contentType(MediaType.APPLICATION_JSON)
    .bodyValue(request)
    .retrieve()
    .bodyToMono(DotNetHumorResponse.class)
    .timeout(Duration.ofSeconds(10))
    .block();
```

### **5. Corpo da Requisição para .NET**
```json
{
  "resumo": "Sinais de sofrimento significativo detectados",
  "nivelGravidade": 2,
  "descricaoOriginal": "Estou com ansiedade",
  "prioridade": true
}
```

### **6. Resposta Esperada do .NET**
```json
{
  "mensagem": "Recomendamos que você busque apoio profissional...",
  "orientacao": "Considere conversar com um psicólogo...",
  "recursosSugeridos": "CVV: 188, Psicólogo online...",
  "sucessoProcessamento": true
}
```

### **7. Resposta Final para Frontend**
```json
{
  "resumo": "Sinais de sofrimento significativo detectados",
  "nivelGravidade": 2,
  "mensagem": "Recomendamos que você busque apoio profissional...",
  "orientacao": "Considere conversar com um psicólogo...",
  "requerAtencaoImediata": true,
  "fonte": "SISTEMA_NET"
}
```

---

## 📊 Quando Envia para .NET

| Nível | Descrição | Envia para .NET? | Prioridade |
|-------|-----------|------------------|------------|
| **0** | Leve | ✅ Sim | ❌ Normal |
| **1** | Moderado | ✅ Sim | ❌ Normal |
| **2** | Grave | ✅ Sim | ⚠️ **ALTA** |
| **3** | Gravíssimo | ❌ **NÃO** | 🚨 Emergência (Java responde) |

---

## 🛠️ Como o .NET Deve Estar Configurado

### **Endpoint Esperado:**
```csharp
[HttpPost]
[Route("api/humor/analise")]
public IActionResult AnalisarHumor([FromBody] HumorRequest request)
{
    // Processar análise
    
    return Ok(new HumorResponse
    {
        Mensagem = "Sua mensagem aqui",
        Orientacao = "Suas orientações aqui",
        RecursosSugeridos = "CVV: 188, etc",
        SucessoProcessamento = true
    });
}
```

### **Modelo .NET (C#):**
```csharp
public class HumorRequest
{
    public string Resumo { get; set; }
    public int NivelGravidade { get; set; }
    public string DescricaoOriginal { get; set; }
    public bool Prioridade { get; set; }
}

public class HumorResponse
{
    public string Mensagem { get; set; }
    public string Orientacao { get; set; }
    public string RecursosSugeridos { get; set; }
    public bool SucessoProcessamento { get; set; }
}
```

---

## 🧪 Testando a Integração

### **Sem .NET Rodando (Comportamento Atual):**

Quando o .NET não está disponível, o Java retorna resposta de erro padrão:

```java
// DotNetIntegrationService.java - Linha 64-70
private DotNetHumorResponse criarRespostaErro() {
    return new DotNetHumorResponse(
        "Não foi possível processar sua solicitação no momento. Por favor, tente novamente.",
        "Se o problema persistir, entre em contato com o suporte.",
        "CVV: 188 | SAMU: 192 | Emergência: 190",
        false
    );
}
```

**Resposta que você recebe:**
```json
{
  "resumo": "...",
  "nivelGravidade": 1,
  "mensagem": "Não foi possível processar sua solicitação no momento...",
  "orientacao": "Se o problema persistir, entre em contato com o suporte.",
  "requerAtencaoImediata": false,
  "fonte": "SISTEMA_NET"
}
```

### **Com .NET Rodando:**

Você receberá a resposta real do sistema .NET com orientações personalizadas.

---

## 🚀 Como Iniciar o Sistema .NET

### **Opção 1: Criar Mock Simples**

Se você não tem o .NET ainda, pode criar um mock rápido:

```bash
# Usando json-server (Node.js)
npm install -g json-server

# Criar arquivo db.json
echo '{
  "humor": {
    "mensagem": "Obrigado por compartilhar. Recomendamos buscar apoio profissional.",
    "orientacao": "Considere conversar com um psicólogo ou terapeuta.",
    "recursosSugeridos": "CVV: 188 | Psicólogo Online: www.exemplo.com",
    "sucessoProcessamento": true
  }
}' > db.json

# Iniciar servidor na porta 5000
json-server --watch db.json --port 5000 --routes routes.json
```

### **Opção 2: Configurar URL Diferente**

Se seu .NET está em outra porta/servidor:

```properties
# application.properties
dotnet.api.url=http://seu-servidor:porta
```

---

## 📝 Logs da Integração

Quando envia para .NET, você verá nos logs:

```
INFO  - Nível 2 detectado - Enviando para sistema .NET
INFO  - Enviando análise para sistema .NET - Nível: 2, Prioridade: true
INFO  - Resposta recebida do sistema .NET com sucesso
```

Se .NET não estiver disponível:
```
INFO  - Enviando análise para sistema .NET - Nível: 1, Prioridade: false
ERROR - Erro ao comunicar com sistema .NET: Connection refused
```

---

## 🔧 Alterando a URL do .NET

### **Método 1: application.properties**
```properties
dotnet.api.url=http://localhost:5000
```

### **Método 2: Variável de Ambiente**
```bash
export DOTNET_API_URL=http://localhost:5000
java -jar lyra.jar
```

### **Método 3: Argumento na Linha de Comando**
```bash
java -jar lyra.jar --dotnet.api.url=http://localhost:5000
```

---

## ✅ Checklist de Integração

- [x] Serviço Java criado (`DotNetIntegrationService.java`)
- [x] Configuração definida (`application.properties`)
- [x] DTOs criados (`DotNetHumorRequest`, `DotNetHumorResponse`)
- [x] Tratamento de erros implementado
- [x] Timeout configurado (10 segundos)
- [x] Logs detalhados
- [ ] Sistema .NET rodando (opcional para testes)

---

## 🎯 Resumo

**Localização da Integração:**
1. **Serviço**: `DotNetIntegrationService.java` (linha 31-61)
2. **Chamada**: `HumorAnalysisService.java` (linha 88)
3. **Endpoint**: `POST http://localhost:5000/api/humor/analise`

**O sistema funciona mesmo sem .NET**, retornando mensagens padrão de erro/orientação.

Para produção, configure o .NET e atualize a URL em `application.properties`! 🚀
