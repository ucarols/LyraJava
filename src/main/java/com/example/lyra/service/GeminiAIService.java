package com.example.lyra.service;

import com.example.lyra.dto.response.GeminiAnalysisResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class GeminiAIService {
    
    private static final Logger logger = LoggerFactory.getLogger(GeminiAIService.class);
    
    private final WebClient.Builder webClientBuilder;
    private final ObjectMapper objectMapper;
    
    @Value("${gemini.api.key}")
    private String apiKey;
    
    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}")
    private String geminiApiUrl;
    
    // Analisa o humor do usuário usando Gemini AI via REST API
    public GeminiAnalysisResponse analisarHumor(String descricao) {
        try {
            String prompt = construirPrompt(descricao);
            
            logger.info("Enviando análise de humor para Gemini AI");
            
            // Monta o corpo da requisição para a API do Gemini
            Map<String, Object> requestBody = construirRequestBody(prompt);
            
            logger.debug("URL Gemini: {}", geminiApiUrl);
            logger.debug("Request body: {}", requestBody);
            
            WebClient webClient = webClientBuilder.build();
            
            // Faz a chamada para a API do Gemini
            String response = webClient
                    .post()
                    .uri(geminiApiUrl + "?key=" + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(requestBody)
                    .retrieve()
                    .onStatus(
                        status -> status.is4xxClientError() || status.is5xxServerError(),
                        clientResponse -> {
                            logger.error("Erro HTTP do Gemini - Status: {}", clientResponse.statusCode());
                            return clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    logger.error("Corpo do erro: {}", errorBody);
                                    return Mono.error(new RuntimeException("Erro na API Gemini: " + errorBody));
                                });
                        }
                    )
                    .bodyToMono(String.class)
                    .timeout(Duration.ofSeconds(30))
                    .onErrorResume(e -> {
                        logger.error("Erro ao comunicar com Gemini AI: {}", e.getMessage(), e);
                        return Mono.just("{\"error\": \"Erro na comunicação\"}");
                    })
                    .block();
            
            logger.info("Resposta recebida do Gemini");
            logger.debug("Resposta completa: {}", response);
            
            // Extrai o texto da resposta do Gemini
            String textoResposta = extrairTextoResposta(response);
            logger.debug("Texto extraído: {}", textoResposta);
            
            // Remove possíveis marcações de código markdown
            String jsonLimpo = limparRespostaJSON(textoResposta);
            
            logger.info("JSON extraído: {}", jsonLimpo);
            
            // Valida se o JSON tem conteúdo
            if (jsonLimpo == null || jsonLimpo.trim().isEmpty() || jsonLimpo.equals("{}")) {
                logger.error("JSON vazio ou inválido retornado pelo Gemini");
                return new GeminiAnalysisResponse(
                    "Não foi possível processar a análise - resposta vazia", 
                    1
                );
            }
            
            GeminiAnalysisResponse analysisResponse = objectMapper.readValue(jsonLimpo, GeminiAnalysisResponse.class);
            
            // Valida se os campos essenciais estão presentes
            if (analysisResponse.getNivel() == null) {
                logger.warn("Campo 'Nivel' ausente na resposta do Gemini - definindo como 1");
                analysisResponse.setNivel(1);
            }
            
            if (analysisResponse.getResumoRecebido() == null || analysisResponse.getResumoRecebido().isEmpty()) {
                logger.warn("Campo 'ResumoRecebido' ausente na resposta do Gemini");
                analysisResponse.setResumoRecebido("Análise não disponível");
            }
            
            return analysisResponse;
            
        } catch (JsonProcessingException e) {
            logger.error("Erro ao processar resposta do Gemini: {}", e.getMessage());
            return new GeminiAnalysisResponse(
                "Não foi possível processar a análise completa", 
                1
            );
        } catch (Exception e) {
            logger.error("Erro ao comunicar com Gemini AI: {}", e.getMessage(), e);
            return criarRespostaFallback(descricao);
        }
    }
    
    // Cria uma resposta de fallback quando o Gemini falha
    private GeminiAnalysisResponse criarRespostaFallback(String descricao) {
        logger.warn("Usando análise de fallback para: {}", descricao.substring(0, Math.min(50, descricao.length())));
        
        // Análise simples baseada em palavras-chave
        String descricaoLower = descricao.toLowerCase();
        int nivel = 1; // Padrão: moderado
        String resumo = "Análise automática - recomenda-se avaliação profissional";
        
        // Palavras que indicam gravidade extrema
        if (descricaoLower.contains("suicid") || descricaoLower.contains("morrer") || 
            descricaoLower.contains("acabar com") || descricaoLower.contains("não aguento mais")) {
            nivel = 3;
            resumo = "Sinais de risco extremo detectados - atenção imediata necessária";
        }
        // Palavras que indicam gravidade alta
        else if (descricaoLower.contains("ansiedade") || descricaoLower.contains("depressão") ||
                 descricaoLower.contains("sobrecarregado") || descricaoLower.contains("não consigo")) {
            nivel = 2;
            resumo = "Sinais de sofrimento significativo detectados";
        }
        // Palavras positivas
        else if (descricaoLower.contains("feliz") || descricaoLower.contains("ótimo") ||
                 descricaoLower.contains("produtivo") || descricaoLower.contains("realizado")) {
            nivel = 0;
            resumo = "Relato com indicadores positivos";
        }
        
        return new GeminiAnalysisResponse(resumo, nivel);
    }
    
    // Constrói o corpo da requisição para a API do Gemini
    private Map<String, Object> construirRequestBody(String prompt) {
        Map<String, Object> content = new HashMap<>();
        Map<String, Object> part = new HashMap<>();
        part.put("text", prompt);
        
        content.put("parts", List.of(part));
        
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("contents", List.of(content));
        
        return requestBody;
    }
    
    // Extrai o texto da resposta JSON do Gemini
    private String extrairTextoResposta(String response) {
        try {
            JsonNode rootNode = objectMapper.readTree(response);
            JsonNode candidates = rootNode.path("candidates");
            
            if (candidates.isArray() && candidates.size() > 0) {
                JsonNode content = candidates.get(0).path("content");
                JsonNode parts = content.path("parts");
                
                if (parts.isArray() && parts.size() > 0) {
                    String text = parts.get(0).path("text").asText();
                    logger.debug("Texto extraído da resposta: {}", text);
                    return text;
                }
            }
            
            logger.warn("Não foi possível extrair texto da resposta");
            return "{}";
            
        } catch (Exception e) {
            logger.error("Erro ao extrair texto da resposta: {}", e.getMessage());
            return "{}";
        }
    }
    
    // Constrói o prompt para enviar ao Gemini
    private String construirPrompt(String descricao) {
        return """
            Você é um assistente especializado em análise de saúde mental e bem-estar emocional.
            
            Analise o seguinte relato de humor do usuário e retorne APENAS um JSON válido no formato especificado.
            
            RELATO DO USUÁRIO:
            "%s"
            
            INSTRUÇÕES IMPORTANTES:
            1. Analise o conteúdo emocional e psicológico do relato
            2. Identifique sinais de sofrimento, angústia ou risco
            3. Classifique o nível de gravidade de 0 a 3:
               - 0 = Leve (situação cotidiana, sem sinais de risco)
               - 1 = Moderado (algum desconforto emocional, mas gerenciável)
               - 2 = Grave (sofrimento significativo, necessita atenção)
               - 3 = Gravíssimo (risco extremo, possível ideação suicida ou autolesão)
            
            4. Gere um resumo breve e empático (máximo 150 caracteres)
            
            REGRAS OBRIGATÓRIAS:
            - Retorne APENAS o JSON, sem texto adicional
            - NÃO use markdown (sem ```)
            - NÃO adicione explicações
            - O campo "Nivel" DEVE ser um número inteiro de 0 a 3
            - O campo "ResumoRecebido" DEVE ser uma string não vazia
            
            FORMATO EXATO DA RESPOSTA:
            {"ResumoRecebido": "seu resumo aqui", "Nivel": 1}
            
            Exemplo de resposta válida:
            {"ResumoRecebido": "Frustração temporária relacionada ao trabalho", "Nivel": 1}
            
            Agora analise o relato acima e retorne o JSON.
            """.formatted(descricao);
    }
    
    // Remove marcações de código markdown da resposta
    private String limparRespostaJSON(String response) {
        return response
                .replaceAll("```json", "")
                .replaceAll("```", "")
                .trim();
    }
}
