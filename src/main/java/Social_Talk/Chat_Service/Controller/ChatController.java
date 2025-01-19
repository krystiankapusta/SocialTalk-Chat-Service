package Social_Talk.Chat_Service.Controller;

import Social_Talk.Chat_Service.Model.Message;
import Social_Talk.Chat_Service.Service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")

public class ChatController {

    @Autowired
    private ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<String> sendMessage(
            @RequestParam int senderId,
            @RequestParam int receiverId,
            @RequestBody String content) {
        chatService.sendMessage(senderId, receiverId, content);
        return ResponseEntity.ok("Message sent successfully!");
    }

    @GetMapping("/conversation")
    public ResponseEntity<List<Message>> getConversation(
            @RequestParam int userId1,
            @RequestParam int userId2) {
        List<Message> messages = chatService.getMessages(userId1, userId2);
        return ResponseEntity.ok(messages);
    }

}