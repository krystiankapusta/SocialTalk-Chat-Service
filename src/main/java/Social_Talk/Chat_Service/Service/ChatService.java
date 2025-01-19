package Social_Talk.Chat_Service.Service;

import Social_Talk.Chat_Service.Config.RabbitMQConfig;
import Social_Talk.Chat_Service.Model.Message;
import Social_Talk.Chat_Service.Repository.MessageRepository;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpMethod;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatService {
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private final RabbitTemplate rabbitTemplate;
    @Autowired
    private final MessageRepository messageRepository;
    private static final Logger logger = LoggerFactory.getLogger(ChatService.class);
    @Value("${url_friends_service}")
    private String urlFriendsService;


    @Autowired
    public ChatService(RabbitTemplate rabbitTemplate, MessageRepository messageRepository) {
        this.rabbitTemplate = rabbitTemplate;
        this.messageRepository = messageRepository;
    }

    public void sendMessage(int senderId, int receiverId, String content) {

        String url = urlFriendsService + senderId + "&userId2=" + receiverId;
        String jwtToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + jwtToken);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        try {
            ResponseEntity<Boolean> response = restTemplate.exchange(
                    url, HttpMethod.GET, requestEntity, Boolean.class);

            Boolean areFriends = response.getBody();
            if (areFriends == null || !areFriends) {
                throw new IllegalArgumentException("Users are not friends!");
            }
        } catch (Exception e) {
            System.err.println("Error while verifying friendship: " + e.getMessage());
            throw e;
        }

        if (senderId == receiverId) {
            throw new IllegalArgumentException("Sender and receiver cannot be the same.");
        }

        if (content == null || content.trim().isEmpty()) {
            throw new IllegalArgumentException("Content cannot be empty.");
        }

        try {
            Message message = new Message();
            message.setSenderId(senderId);
            message.setReceiverId(receiverId);
            message.setContent(content);
            message.setSentMessageAt(LocalDateTime.now());

            messageRepository.save(message);

            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.chat_exchange,
                    RabbitMQConfig.chat_routing_key,
                    message
            );
            System.out.println("Message has been sent to queue: " + RabbitMQConfig.chat_queue);

        } catch (Exception e) {
            logger.error("Error while sending message: ", e);
            throw e;
        }
    }

    public List<Message> getMessages(int userId, int friendId) {
        return messageRepository.findMessagesBetweenUsers(userId, friendId);
    }

}
