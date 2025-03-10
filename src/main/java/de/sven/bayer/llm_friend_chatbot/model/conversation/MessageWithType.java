package de.sven.bayer.llm_friend_chatbot.model.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.ai.chat.messages.*;

@AllArgsConstructor
@Data
public class MessageWithType {

    private final String text;
    private final MessageType messageType;

    public static Message createMessage(MessageWithType message) {
        String text = message.getText();
        MessageType messageType = message.getMessageType();
        return switch (messageType) {
            case SYSTEM -> new SystemMessage(text);
            case USER -> new UserMessage(text);
            case ASSISTANT -> new AssistantMessage(text);
            default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
        };
    }

    public static MessageWithType createMessageWithType(Message message) {
        String text = message.getText();
        MessageType messageType = message.getMessageType();
        return new MessageWithType(text, messageType);
    }
}
