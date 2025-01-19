package Social_Talk.Chat_Service.Listener;

import Social_Talk.Chat_Service.Config.RabbitMQConfig;
import Social_Talk.Chat_Service.Model.Message;
import Social_Talk.Chat_Service.Repository.MessageRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ChatListener {
    @Autowired
    private MessageRepository messageRepository;

    @RabbitListener(queues = RabbitMQConfig.chat_queue)
    public void receiveMessage(Message message) {
        System.out.println("Received message from user " + message.getSenderId() + ": " + message.getContent());
        System.out.println("Message sent at: " + message.getSentMessageAt());

    }
}