package Social_Talk.Chat_Service.Repository;

import Social_Talk.Chat_Service.Model.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT m FROM Message m WHERE " +
            "(m.senderId = :userId AND m.receiverId = :friendId) OR " +
            "(m.senderId = :friendId AND m.receiverId = :userId) " +
            "ORDER BY m.sentMessageAt ASC")
    List<Message> findMessagesBetweenUsers(int userId, int friendId);
}