package com.example.lyra.consumer;

import com.example.lyra.dto.request.HumorAnalysisRequest;
import com.example.lyra.dto.response.GeminiAnalysisResponse;
import com.example.lyra.service.GeminiAIService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

/**
 * Serviço responsável por consumir mensagens da fila de atualizações de humor
 */
@Service
public class RabbitMQConsumer {

    // Logger para registrar as operações
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);
    private final GeminiAIService geminiAIService;

    public RabbitMQConsumer(GeminiAIService geminiAIService) {
        this.geminiAIService = geminiAIService;
    }

    /**
     * Processa as mensagens recebidas da fila de humor
     * @param request Objeto contendo os dados do humor do usuário
     */
    @RabbitListener(queues = "${rabbitmq.queue.humor}")
    public void consume(HumorAnalysisRequest request) {

        LOGGER.info("Mensagem recebida da fila | Humor = {}, Descrição = {}",
                request.getNivelHumorOriginal(), request.getDescricao());

        GeminiAnalysisResponse response = geminiAIService.analisarHumor(
                request.getNivelHumorOriginal(), request.getDescricao()
        );

        LOGGER.info("🤖 Resposta da IA: Nivel = {}, Resumo = {}",
                response.getNivel(), response.getResumoRecebido());

        // Mantido para futuras integraçoes

//        DotNetHumorRequest request = new DotNetHumorRequest(
//                message.getDescricao(),
//                message.getNivel(),
//                null,
//                true
//        );

        //integrationService.enviarParaDotNet(request);
    }
}
