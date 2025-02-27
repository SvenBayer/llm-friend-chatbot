package de.sven.bayer.llm_friend_chatbot.model.conversation;

import lombok.Data;

@Data
public class MessageFromUser {
    private String message;
    private String conversationId;
}
