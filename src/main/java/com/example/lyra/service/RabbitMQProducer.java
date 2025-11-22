package com.example.lyra.service;

import com.example.lyra.dto.request.HumorAnalysisRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RabbitMQProducer {

    // Logger para registrar as operações
    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    // Nome do exchange configurado no RabbitMQ
    @Value("${rabbitmq.exchange}")
    private String exchange;

    // Chave de roteamento para as mensagens
    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    // Cliente para envio de mensagens ao RabbitMQ
    private final RabbitTemplate rabbitTemplate;

    /**
     * Construtor com injeção de dependência
     * @param rabbitTemplate Instância do RabbitTemplate para envio de mensagens
     */
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendToGemini(HumorAnalysisRequest request){
        LOGGER.info("[RabbitMQ] Enviando relato para IA - Humor: {}, descricao: {}",
                request.getNivelHumorOriginal(), request.getDescricao());

        rabbitTemplate.convertAndSend(exchange, routingKey, request);
        LOGGER.info("[RabbitMQ] Mensagem enviada para análise da IA");
    }
}
