package Social_Talk.Chat_Service.Listener;

import Social_Talk.Chat_Service.Config.JwtAuthenticationFilter;
import Social_Talk.Chat_Service.Config.RabbitMQConfig;
import Social_Talk.Chat_Service.Model.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;


@Component
public class ChatListener {

    private static final Logger logger = LoggerFactory.getLogger(ChatListener.class);

    @RabbitListener(queues = RabbitMQConfig.chat_queue)
    public void receiveMessage(Message message) {
        logger.info("Received message from user {} : {}", message.getSenderId(), message.getContent());
        logger.info("Message sent at: {}", message.getSentMessageAt());
    }
}