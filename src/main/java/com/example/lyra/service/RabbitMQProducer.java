package com.example.lyra.service;

import com.example.lyra.dto.HumorMessage;
import com.example.lyra.model.EHumor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class RabbitMQProducer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQProducer.class);

    @Value("${rabbitmq.exchange}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendHumorUpdate(Long userId, String userName, EHumor humor, String descricao) {
        LOGGER.info(String.format("Enviando atualização de humor para o usuário: %s", userName));
        
        HumorMessage message = new HumorMessage(
            userId,
            userName,
            humor,
            descricao,
            LocalDateTime.now()
        );
        
        rabbitTemplate.convertAndSend(exchange, routingKey, message);
    }
}
