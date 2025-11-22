package com.example.lyra.service;

import com.example.lyra.dto.request.DotNetHumorRequest;
import com.example.lyra.dto.response.DotNetHumorResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class DotNetIntegrationService {
    
    private static final Logger logger = LoggerFactory.getLogger(DotNetIntegrationService.class);
    
    private final WebClient.Builder webClientBuilder;
    
    @Value("${dotnet.api.url}")
    private String dotNetApiUrl;
    
    @Value("${dotnet.api.timeout:10}")
    private Integer timeoutSeconds;
    
    // Envia análise de humor para o sistema .NET
    public DotNetHumorResponse enviarParaDotNet(DotNetHumorRequest request) {
        try {
            logger.info("Enviando análise para sistema .NET - Nível: {}, Prioridade: {}", 
                request.getNivelGravidade(), request.getPrioridade());
            
            WebClient webClient = webClientBuilder
                    .baseUrl(dotNetApiUrl)
                    .build();
            
            DotNetHumorResponse response = webClient
                    .post()
                    .uri("/api/v1/ai/solicitar")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(DotNetHumorResponse.class)
                    .timeout(Duration.ofSeconds(timeoutSeconds))
                    .onErrorResume(e -> {
                        logger.error("Erro ao comunicar com sistema .NET: {}", e.getMessage());
                        return Mono.just(criarRespostaErro());
                    })
                    .block();
            
            logger.info("Resposta recebida do sistema .NET com sucesso");
            return response;
            
        } catch (Exception e) {
            logger.error("Erro ao processar requisição para .NET: {}", e.getMessage());
            return criarRespostaErro();
        }
    }
    
    // Cria resposta de erro padrão
    private DotNetHumorResponse criarRespostaErro() {
        return new DotNetHumorResponse(
            "Não foi possível processar sua solicitação no momento. Por favor, tente novamente.",
            "Se o problema persistir, entre em contato com o suporte.",
            "CVV: 188 | SAMU: 192 | Emergência: 190",
            false
        );
    }
}
