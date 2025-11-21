# 📊 Fluxo Completo de Dados - Java → Gemini → .NET

## 🔄 Fluxo Visual Detalhado

```
┌─────────────────────────────────────────────────────────────────┐
│ 1. FRONTEND ENVIA                                               │
└─────────────────────────────────────────────────────────────────┘
{
  "nivelHumorOriginal": "NAO_TAO_BEM",
  "descricao": "Estou com muita ansiedade e não consigo dormir"
}
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 2. JAVA RECEBE E VALIDA                                         │
│    HumorAnalysisController.java                                 │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 3. ENVIA PARA GEMINI AI                                         │
│    GeminiAIService.analisarHumor()                              │
└─────────────────────────────────────────────────────────────────┘
Envia: "Estou com muita ansiedade e não consigo dormir"
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 4. GEMINI ANALISA E RETORNA                                     │
└─────────────────────────────────────────────────────────────────┘
{
  "ResumoRecebido": "Sinais de ansiedade e insônia",  ← RESUMO GERADO
  "Nivel": 2                                           ← NÍVEL DETECTADO
}
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 5. JAVA DECIDE AÇÃO (HumorAnalysisService.java)                │
└─────────────────────────────────────────────────────────────────┘
Nível = 2 (Grave) → Envia para .NET com PRIORIDADE
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 6. MONTA REQUISIÇÃO PARA .NET                                   │
│    Linha 81-86 do HumorAnalysisService.java                     │
└─────────────────────────────────────────────────────────────────┘
DotNetHumorRequest {
  "resumo": "Sinais de ansiedade e insônia",        ← RESUMO DO GEMINI
  "nivelGravidade": 2,                              ← NÍVEL DO GEMINI
  "descricaoOriginal": "Estou com muita ansiedade...", ← TEXTO ORIGINAL
  "prioridade": true                                ← PRIORIDADE (nível 2)
}
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 7. ENVIA PARA .NET                                              │
│    DotNetIntegrationService.enviarParaDotNet()                  │
│    POST http://localhost:5000/api/humor/analise                 │
└─────────────────────────────────────────────────────────────────┘
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 8. .NET PROCESSA E RETORNA                                      │
└─────────────────────────────────────────────────────────────────┘
{
  "mensagem": "Recomendamos buscar apoio profissional...",
  "orientacao": "Considere terapia cognitivo-comportamental...",
  "recursosSugeridos": "CVV: 188, Psicólogo online...",
  "sucessoProcessamento": true
}
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 9. JAVA MONTA RESPOSTA FINAL                                    │
│    Linha 90-97 do HumorAnalysisService.java                     │
└─────────────────────────────────────────────────────────────────┘
{
  "resumo": "Sinais de ansiedade e insônia",       ← DO GEMINI
  "nivelGravidade": 2,                             ← DO GEMINI
  "mensagem": "Recomendamos buscar apoio...",      ← DO .NET
  "orientacao": "Considere terapia...",            ← DO .NET
  "requerAtencaoImediata": true,                   ← JAVA (nível 2)
  "fonte": "SISTEMA_NET"                           ← JAVA
}
                              ↓
┌─────────────────────────────────────────────────────────────────┐
│ 10. RETORNA PARA FRONTEND                                       │
└─────────────────────────────────────────────────────────────────┘
```

---

## 📋 Resumo: O Que Vai Para o .NET?

### **SIM, envia 4 campos:**

| Campo | Origem | Exemplo |
|-------|--------|---------|
| `resumo` | ✅ **Gemini AI** | "Sinais de ansiedade e insônia" |
| `nivelGravidade` | ✅ **Gemini AI** | 2 |
| `descricaoOriginal` | ✅ **Frontend** | "Estou com muita ansiedade..." |
| `prioridade` | ✅ **Java** | true (se nível = 2) |

---

## 💻 Código Exato (Linha 81-86)

```java
DotNetHumorRequest dotNetRequest = new DotNetHumorRequest(
    resumo,              // ← RESUMO DO GEMINI
    nivel,               // ← NÍVEL DO GEMINI (0-3)
    descricaoOriginal,   // ← TEXTO ORIGINAL DO USUÁRIO
    prioridade           // ← true se nível = 2, false se nível = 0 ou 1
);
```

---

## 🎯 Exemplo Real Completo

### **Entrada do Usuário:**
```json
{
  "nivelHumorOriginal": "NAO_TAO_BEM",
  "descricao": "Estou com muita ansiedade e não consigo dormir há dias"
}
```

### **Gemini Retorna:**
```json
{
  "ResumoRecebido": "Quadro de ansiedade com insônia persistente",
  "Nivel": 2
}
```

### **Java Envia para .NET:**
```json
POST http://localhost:5000/api/humor/analise

{
  "resumo": "Quadro de ansiedade com insônia persistente",
  "nivelGravidade": 2,
  "descricaoOriginal": "Estou com muita ansiedade e não consigo dormir há dias",
  "prioridade": true
}
```

### **.NET Retorna:**
```json
{
  "mensagem": "Identificamos sinais de ansiedade que requerem atenção. Recomendamos buscar apoio profissional.",
  "orientacao": "Considere agendar consulta com psicólogo ou psiquiatra. Técnicas de relaxamento podem ajudar.",
  "recursosSugeridos": "CVV: 188 | Psicólogo Online: www.exemplo.com",
  "sucessoProcessamento": true
}
```

### **Java Retorna para Frontend:**
```json
{
  "resumo": "Quadro de ansiedade com insônia persistente",
  "nivelGravidade": 2,
  "mensagem": "Identificamos sinais de ansiedade que requerem atenção...",
  "orientacao": "Considere agendar consulta com psicólogo...",
  "requerAtencaoImediata": true,
  "fonte": "SISTEMA_NET"
}
```

---

## 🔍 Quando NÃO Envia para .NET?

### **Apenas Nível 3 (Gravíssimo):**

```
Gemini retorna: { "Nivel": 3 }
                     ↓
Java detecta nível 3
                     ↓
❌ NÃO envia para .NET
                     ↓
✅ Gera resposta de emergência (CVV: 188)
                     ↓
Retorna direto para Frontend
```

**Código (linha 56-58):**
```java
else if (nivel == 3) {
    logger.error("Nível GRAVÍSSIMO detectado - Gerando resposta de emergência");
    return gerarRespostaEmergencia(resumo, nivel);  // ← NÃO vai para .NET!
}
```

--