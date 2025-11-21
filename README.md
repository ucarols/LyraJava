# 🌟 Bem-vindo ao Lyra!

<div align="center">

![Lyra Logo](https://via.placeholder.com/200x200/4A90E2/FFFFFF?text=Lyra)

**Sistema de Apoio ao Bem-Estar Emocional de Trabalhadores**

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

[Pitch](#-vídeos) • [Documentação](#-documentação) • [Como Rodar](#-como-rodar) • [API](#-exemplos-de-requisições)

</div>

---

## 📖 Sobre o Projeto

O **Lyra** é uma aplicação voltada para apoiar o bem-estar emocional de trabalhadores.

Através de relatos enviados pelo usuário, o sistema identifica o nível de risco emocional e gera recomendações simples que podem ajudar no momento. Cada interação é registrada, permitindo acompanhar como o estado emocional evolui ao longo do tempo.

O objetivo do Lyra é oferecer um apoio rápido, acessível e acolhedor para quem enfrenta estresse e sobrecarga na rotina de trabalho.

### 🎯 Funcionalidades Principais

- 🤖 **Análise com IA Generativa** - Integração com Google Gemini AI para análise de sentimentos
- 📊 **Classificação de Risco** - Identificação automática de níveis de gravidade emocional (0-3)
- 💬 **Recomendações Personalizadas** - Orientações adaptadas ao estado emocional do usuário
- 📈 **Histórico de Humor** - Acompanhamento da evolução emocional ao longo do tempo
- 🔔 **Alertas de Emergência** - Detecção de situações críticas com encaminhamento para canais de apoio
- 🌐 **Suporte Multilíngue** - Interface em Português e Inglês
- 🔐 **Segurança** - Autenticação JWT e controle de acesso por roles

---

## 👥 Integrantes

| Nome | Turma | RM |
|------|-------|-----|
| **Caroline de Oliveira** | 2TDSB | RM559123 |
| **Giulia Corrêa Camillo** | 2TDSB | RM554473 |
| **Lavinia Soo Hyun Park** | 2TDSB | RM554473 |

---

## 🎥 Vídeos

### 📹 Pitch do Projeto
[![Pitch Lyra](https://img.shields.io/badge/YouTube-Assistir%20Pitch-red?style=for-the-badge&logo=youtube)](link-dps-aqui)

> 🔗 **Link:** [link-dps-aqui](link-dps-aqui)

### 🎬 Demonstração Técnica
[![Demo Lyra](https://img.shields.io/badge/YouTube-Ver%20Demonstração-red?style=for-the-badge&logo=youtube)](link-dps-aqui)

> 🔗 **Link:** [link-dps-aqui](link-dps-aqui)

---

## 🚀 Deploy

### 🌐 API em Produção
[![API Status](https://img.shields.io/badge/Status-Online-success?style=for-the-badge)](link-dps-aqui)

> 🔗 **URL da API:** [link-dps-aqui](link-dps-aqui)

### 📨 Mensageria (RabbitMQ)
[![RabbitMQ Status](https://img.shields.io/badge/RabbitMQ-Online-orange?style=for-the-badge)](link-dps-aqui)

> 🔗 **URL RabbitMQ:** [link-dps-aqui](link-dps-aqui)  
> 🔗 **Management Console:** [link-dps-aqui](link-dps-aqui)

---

## 🏗️ Arquitetura do Sistema

### 📐 Diagrama de Arquitetura

```
┌─────────────────────────────────────────────────────────────────┐
│                         FRONTEND                                │
│                    (React/Angular/Vue)                          │
└────────────────────────┬────────────────────────────────────────┘
                         │ HTTP/REST
                         ↓
┌─────────────────────────────────────────────────────────────────┐
│                      SPRING BOOT API                            │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │ Controllers  │  │   Services   │  │ Repositories │          │
│  │  - Auth      │  │  - User      │  │  - User      │          │
│  │  - User      │  │  - Humor     │  │  - Role      │          │
│  │  - Humor     │  │  - Gemini AI │  └──────────────┘          │
│  └──────────────┘  └──────────────┘                            │
│                                                                  │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐          │
│  │   Security   │  │    Cache     │  │  Messaging   │          │
│  │  - JWT Auth  │  │  - Spring    │  │  - RabbitMQ  │          │
│  │  - Roles     │  │    Cache     │  │  - Producer  │          │
│  └──────────────┘  └──────────────┘  │  - Consumer  │          │
│                                       └──────────────┘          │
└────┬─────────────────┬─────────────────┬────────────────────────┘
     │                 │                 │
     ↓                 ↓                 ↓
┌─────────┐    ┌──────────────┐    ┌──────────┐
│   H2    │    │  Gemini AI   │    │ RabbitMQ │
│Database │    │   (Google)   │    │  Queue   │
└─────────┘    └──────────────┘    └──────────┘
                      │
                      ↓
              ┌──────────────┐
              │  Sistema     │
              │    .NET      │
              │ (Opcional)   │
              └──────────────┘
```

### 🔄 Fluxo de Análise de Humor

```
1. Usuário envia relato
         ↓
2. API valida e autentica
         ↓
3. Gemini AI analisa o texto
         ↓
4. Sistema classifica gravidade (0-3)
         ↓
5. Decisão baseada no nível:
   ├─ Nível 0-1: Envia para .NET (normal)
   ├─ Nível 2: Envia para .NET (PRIORIDADE)
   └─ Nível 3: Gera alerta de emergência
         ↓
6. Retorna orientações ao usuário
         ↓
7. Registra no histórico
         ↓
8. Envia notificação via RabbitMQ
```

### 🧩 Componentes Principais

| Componente | Tecnologia | Função |
|------------|------------|--------|
| **Backend** | Spring Boot 3.5.7 | API REST principal |
| **IA** | Google Gemini AI | Análise de sentimentos |
| **Banco de Dados** | H2 Database | Persistência de dados |
| **Cache** | Spring Cache | Otimização de performance |
| **Mensageria** | RabbitMQ | Comunicação assíncrona |
| **Segurança** | Spring Security + JWT | Autenticação e autorização |
| **Validação** | Bean Validation | Validação de dados |
| **Integração** | WebClient | Comunicação com APIs externas |

---

## 📡 Exemplos de Requisições

### 🔐 Autenticação

#### Registrar Novo Usuário
```bash
POST /api/auth/signup
Content-Type: application/json

{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao@exemplo.com",
  "password": "senha123",
  "roles": ["user"]
}
```

**Resposta:**
```json
{
  "message": "Usuário registrado com sucesso!"
}
```

#### Login
```bash
POST /api/auth/signin
Content-Type: application/json

{
  "email": "joao@exemplo.com",
  "password": "senha123"
}
```

**Resposta:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "type": "Bearer",
  "id": 1,
  "email": "joao@exemplo.com",
  "roles": ["ROLE_USER"]
}
```

---

### 🧠 Análise de Humor

#### Enviar Relato para Análise
```bash
POST /api/humor/analisar
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "nivelHumorOriginal": "NAO_TAO_BEM",
  "descricao": "Estou me sentindo muito sobrecarregado com o trabalho. Não consigo dormir direito há dias e estou com muita ansiedade."
}
```

**Resposta (Nível 2 - Grave):**
```json
{
  "resumo": "Sobrecarga emocional com sintomas de ansiedade e insônia",
  "nivelGravidade": 2,
  "mensagem": "Identificamos sinais de sofrimento significativo. Recomendamos buscar apoio profissional.",
  "orientacao": "Considere agendar consulta com psicólogo ou psiquiatra. Técnicas de relaxamento podem ajudar.",
  "requerAtencaoImediata": true,
  "fonte": "SISTEMA_NET"
}
```

**Resposta (Nível 3 - Emergência):**
```json
{
  "resumo": "Sinais de risco extremo detectados",
  "nivelGravidade": 3,
  "mensagem": "Percebemos que você está passando por um momento muito difícil...\n\n📞 CVV - Centro de Valorização da Vida\nLigue: 188 (24h, todos os dias)",
  "orientacao": "AÇÃO IMEDIATA NECESSÁRIA:\n1. Ligue para o CVV (188) AGORA\n2. Se estiver em risco imediato, ligue 192 ou 190",
  "requerAtencaoImediata": true,
  "fonte": "SISTEMA_JAVA"
}
```

#### Valores Válidos para `nivelHumorOriginal`
- `OTIMO` - Ótimo
- `BEM` - Bem
- `NEUTRO` - Neutro
- `NAO_TAO_BEM` - Não tão bem
- `DIFICIL` - Difícil

---

### 👤 Gerenciamento de Usuários

#### Listar Todos os Usuários (Admin)
```bash
GET /api/users
Authorization: Bearer {seu_token}
```

#### Listar Usuários com Paginação
```bash
GET /api/users/paginated?page=0&size=10
Authorization: Bearer {seu_token}
```

#### Buscar Usuário por ID
```bash
GET /api/users/{id}
Authorization: Bearer {seu_token}
```

#### Atualizar Usuário
```bash
PUT /api/users/{id}
Authorization: Bearer {seu_token}
Content-Type: application/json

{
  "firstName": "João",
  "lastName": "Silva",
  "email": "joao.novo@exemplo.com"
}
```

#### Deletar Usuário (Admin)
```bash
DELETE /api/users/{id}
Authorization: Bearer {seu_token}
```

---

### 🌐 Internacionalização

#### Trocar Idioma
Adicione o parâmetro `lang` em qualquer requisição:

```bash
GET /api/users?lang=en
GET /api/users?lang=pt-BR
```

---

## 🛠️ Como Rodar

### 📋 Pré-requisitos

- ☕ **Java 17** ou superior
- 📦 **Maven 3.6+**
- 🐰 **RabbitMQ** (opcional, para mensageria)
- 🔑 **API Key do Google Gemini** (já configurada)

### 🚀 Passo a Passo

#### 1️⃣ Clone o Repositório
```bash
git clone https://github.com/seu-usuario/lyra.git
cd lyra
```

#### 2️⃣ Configure as Variáveis de Ambiente (Opcional)

Edite o arquivo `src/main/resources/application.properties`:

```properties
# Gemini AI
gemini.api.key=SUA_API_KEY_AQUI

# .NET Integration (opcional)
dotnet.api.url=http://localhost:5000

# RabbitMQ (opcional)
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
```

#### 3️⃣ Compile o Projeto
```bash
mvn clean install
```

#### 4️⃣ Execute a Aplicação
```bash
mvn spring-boot:run
```

Ou execute o JAR gerado:
```bash
java -jar target/lyra-0.0.1-SNAPSHOT.jar
```

#### 5️⃣ Acesse a Aplicação

- 🌐 **API:** http://localhost:8080
- 📊 **H2 Console:** http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:lyradb`
  - Username: `sa`
  - Password: *(vazio)*

---

### 🐰 Iniciar RabbitMQ (Opcional)

#### Via Docker:
```bash
docker run -d --name rabbitmq \
  -p 5672:5672 \
  -p 15672:15672 \
  rabbitmq:3-management
```

#### Via Homebrew (Mac):
```bash
brew services start rabbitmq
```

**Management Console:** http://localhost:15672  
**Credenciais padrão:** guest / guest

---

## 🧪 Testando a API

### Via cURL

```bash
# 1. Login
curl -X POST http://localhost:8080/api/auth/signin \
  -H "Content-Type: application/json" \
  -d '{"email":"admin@example.com","password":"admin123"}'

# 2. Copie o token da resposta

# 3. Teste análise de humor
curl -X POST http://localhost:8080/api/humor/analisar \
  -H "Authorization: Bearer SEU_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "nivelHumorOriginal": "NAO_TAO_BEM",
    "descricao": "Estou com muita ansiedade"
  }'
```

### Via Postman

1. Importe a collection: `Lyra-Postman-Collection.json`
2. Execute "Login - Obter Token"
3. Execute qualquer teste de "Análise de Humor"

### Via Script Automatizado

```bash
chmod +x exemplos-curl.sh
./exemplos-curl.sh
```

---

## 📚 Documentação

- 📖 [Análise de Requisitos Completa](ANALISE_REQUISITOS_COMPLETA.md)
- 🔗 [Integração com .NET](INTEGRACAO_DOTNET.md)
- 🔄 [Fluxo de Dados Completo](FLUXO_DADOS_COMPLETO.md)
- 🎯 [Valores do Enum Humor](VALORES_ENUM_HUMOR.md)
- 🧪 [Guia de Testes](GUIA_TESTES.md)

---

## 🛡️ Segurança

- 🔐 **Autenticação JWT** - Tokens seguros com expiração
- 👥 **Controle de Acesso** - Roles (USER, ADMIN)
- ✅ **Validação de Dados** - Bean Validation em todas as entradas
- 🔒 **Senhas Criptografadas** - BCrypt
- 🚫 **CORS Configurado** - Proteção contra requisições não autorizadas

---

## 🎯 Níveis de Gravidade

| Nível | Descrição | Ação do Sistema |
|-------|-----------|-----------------|
| **0** | Leve | Envia para .NET (orientações normais) |
| **1** | Moderado | Envia para .NET (orientações normais) |
| **2** | Grave | Envia para .NET com **PRIORIDADE** |
| **3** | Gravíssimo | **Alerta de emergência** - CVV: 188 |

---

## 📞 Canais de Apoio

Em caso de emergência, o sistema recomenda:

- 📞 **CVV (Centro de Valorização da Vida):** 188 - Disponível 24h
- 🚑 **SAMU:** 192
- 🚨 **Emergência:** 190
- 💬 **Chat CVV:** www.cvv.org.br

---

## 🤝 Contribuindo

Contribuições são bem-vindas! Para contribuir:

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

---

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo [LICENSE](LICENSE) para mais detalhes.

---

## 📧 Contato

- 📧 **Email:** lyra.suporte@exemplo.com
- 💼 **LinkedIn:** [Lyra Project](https://linkedin.com/company/lyra)
- 🐦 **Twitter:** [@LyraProject](https://twitter.com/lyraproject)

---

<div align="center">

**Desenvolvido com ❤️ pela equipe Lyra**

[![FIAP](https://img.shields.io/badge/FIAP-2TDSB-red?style=for-the-badge)](https://www.fiap.com.br/)

**2025 - Todos os direitos reservados**

</div>
