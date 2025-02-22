package de.sven.bayer.llm_friend_chatbot.model;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ConversationHistory {

    @Getter
    private final List<ConversationEntry> conversationHistory = new ArrayList<>();

    private final ConfigProperties configProperties;

    public ConversationHistory(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public void addEntry(UserMessage userMessage, LlmAnswer llmAnswer) {
        ConversationEntry entry = new ConversationEntry(userMessage, llmAnswer);
        conversationHistory.add(entry);
    }

    @Override
    public String toString() {
        String llmName = configProperties.llmName;

        StringBuilder historyBuilder = new StringBuilder();
        for (ConversationEntry entry : conversationHistory) {
            historyBuilder.append("USER: ").append(entry.userMessage()).append("\n");
            historyBuilder.append(llmName).append(": ").append(entry.llmAnswer()).append("\n");
        }
        return historyBuilder.toString().trim();
    }
}


