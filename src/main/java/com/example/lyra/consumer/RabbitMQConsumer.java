package com.example.lyra.consumer;

import com.example.lyra.dto.HumorMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class RabbitMQConsumer {

    private static final Logger LOGGER = LoggerFactory.getLogger(RabbitMQConsumer.class);

    @RabbitListener(queues = "${rabbitmq.queue.humor}")
    public void consume(HumorMessage message) {
        LOGGER.info(String.format("Mensagem recebida -> Usuário: %s, Humor: %s, Descrição: %s, Data/Hora: %s",
                message.getUserName(),
                message.getHumor(),
                message.getDescricao(),
                message.getDataHora()));
        
        // Aqui você pode adicionar lógica adicional, como salvar no banco de dados
        // ou enviar notificações
    }
}
