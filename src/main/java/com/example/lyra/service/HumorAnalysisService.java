package com.example.lyra.service;

import com.example.lyra.dto.request.DotNetHumorRequest;
import com.example.lyra.dto.request.HumorAnalysisRequest;
import com.example.lyra.dto.response.DotNetHumorResponse;
import com.example.lyra.dto.response.GeminiAnalysisResponse;
import com.example.lyra.dto.response.HumorAnalysisResponse;
import com.example.lyra.model.EHumor;
import com.example.lyra.model.User;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HumorAnalysisService {
    
    private static final Logger logger = LoggerFactory.getLogger(HumorAnalysisService.class);
    
    private final GeminiAIService geminiAIService;
    private final DotNetIntegrationService dotNetIntegrationService;
    
    // Processa a análise de humor completa
    public HumorAnalysisResponse processarAnaliseHumor(HumorAnalysisRequest request) {
        logger.info("Iniciando análise de humor - Nível original: {}", request.getNivelHumorOriginal());

        // Etapa 1: Enviar para Gemini AI
        GeminiAnalysisResponse geminiResponse = geminiAIService.analisarHumor(request.getNivelHumorOriginal(), request.getDescricao());
        
        logger.info("Análise do Gemini concluída - Nível: {}, Resumo: {}", 
            geminiResponse.getNivel(), geminiResponse.getResumoRecebido());
        
        // Etapa 2: Processar conforme nível de gravidade
        return processarPorNivelGravidade(geminiResponse, request.getDescricao());
    }
    
    // Processa a resposta conforme o nível de gravidade identificado pela IA
    private HumorAnalysisResponse processarPorNivelGravidade(
            GeminiAnalysisResponse geminiResponse, String descricaoOriginal) {
        
        Integer nivel = geminiResponse.getNivel();
        String resumo = geminiResponse.getResumoRecebido();
        
        // Validação: se nivel for null, assume nível moderado (1)
        if (nivel == null) {
            logger.warn("Gemini retornou nível null - Assumindo nível moderado (1)");
            nivel = 1;
            if (resumo == null || resumo.isEmpty()) {
                resumo = "Análise não disponível no momento";
            }
        }
        
        // Nível 0 (Leve), 1 (Moderado) ou 2 (Grave)
        if (nivel == 0 || nivel == 1 || nivel == 2) {
            logger.info("Nível {} detectado - Enviando para sistema .NET", nivel);
            if (nivel == 0 || nivel == 1)
                return enviarParaDotNetENormal(resumo, nivel, descricaoOriginal, false);
            else
                return enviarParaDotNetENormal(resumo, nivel, descricaoOriginal, true);
        }
        
        // Nível 3 (Gravíssimo)
        else if (nivel == 3) {
            logger.error("Nível GRAVÍSSIMO detectado - Gerando resposta de emergência");
            return gerarRespostaEmergencia(resumo, nivel);
        }
        
        // Nível desconhecido (fallback)
        else {
            logger.warn("Nível desconhecido: {} - Tratando como moderado", nivel);
            return enviarParaDotNetENormal(resumo, 1, descricaoOriginal, false);
        }
    }
    
    // Envia para o sistema .NET e retorna resposta
    private HumorAnalysisResponse enviarParaDotNetENormal(
            String resumo, Integer nivel, String descricaoOriginal, Boolean prioridade) {
        
        DotNetHumorRequest dotNetRequest = new DotNetHumorRequest(
            resumo,
            nivel,
            descricaoOriginal,
            prioridade
        );
        
        DotNetHumorResponse dotNetResponse = dotNetIntegrationService.enviarParaDotNet(dotNetRequest);
        
        return new HumorAnalysisResponse(
            resumo,
            nivel,
            dotNetResponse.getMensagem(),
            dotNetResponse.getOrientacao(),
            prioridade,
            "SISTEMA_NET"
        );
    }
    
    // Gera resposta de emergência para casos gravíssimos (nível 3)
    private HumorAnalysisResponse gerarRespostaEmergencia(String resumo, Integer nivel) {
        
        String mensagemApoio = """
            Percebemos que você está passando por um momento muito difícil e queremos que saiba que não está sozinho(a).
            
            O que você está sentindo é importante e merece atenção imediata. Sua vida tem valor e existem pessoas 
            prontas para ajudar você agora mesmo.
            
            Por favor, entre em contato com um dos canais de apoio abaixo IMEDIATAMENTE:
            
            📞 CVV - Centro de Valorização da Vida
            Ligue: 188 (disponível 24h, todos os dias)
            Chat: www.cvv.org.br
            
            🚑 SAMU - Serviço de Atendimento Móvel de Urgência
            Ligue: 192
            
            🚨 Em caso de emergência imediata
            Ligue: 190 (Polícia Militar)
            
            Você também pode:
            • Procurar o pronto-socorro mais próximo
            • Conversar com alguém de confiança (familiar, amigo)
            • Ligar para seu médico ou terapeuta
            
            Lembre-se: Este momento difícil é temporário. Com ajuda adequada, você pode superar isso.
            Sua vida importa. Por favor, busque ajuda agora.
            """;
        
        String orientacao = """
            AÇÃO IMEDIATA NECESSÁRIA:
            1. Ligue para o CVV (188) AGORA - atendimento gratuito e sigiloso
            2. Se estiver em risco imediato, ligue 192 (SAMU) ou 190
            3. Não fique sozinho(a) - procure alguém de confiança
            4. Vá ao pronto-socorro se necessário
            
            Você não precisa enfrentar isso sozinho(a). Ajuda está disponível 24 horas por dia.
            """;
        
        return new HumorAnalysisResponse(
            resumo,
            nivel,
            mensagemApoio,
            orientacao,
            true, // Requer atenção imediata
            "SISTEMA_JAVA"
        );
    }
}
