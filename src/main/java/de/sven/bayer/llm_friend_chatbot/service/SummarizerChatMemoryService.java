package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.client.SummarizerChatMemoryClient;
import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SummarizerChatMemoryService implements ChatMemory {

    private final ConfigProperties configProperties;

    private final Map<String, List<Message>> conversationHistory = new HashMap<>();
    private final SummarizerChatMemoryClient summarizerChatMemoryClient;

    public SummarizerChatMemoryService(ConfigProperties configProperties, SummarizerChatMemoryClient summarizerChatMemoryClient) {
        this.configProperties = configProperties;
        this.summarizerChatMemoryClient = summarizerChatMemoryClient;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        this.conversationHistory.putIfAbsent(conversationId, new ArrayList<>());
        this.conversationHistory.get(conversationId).addAll(messages);

        summarizeHistory(conversationId);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> all = this.conversationHistory.get(conversationId);
        return all != null ? all.stream().skip((long)Math.max(0, all.size() - lastN)).toList() : List.of();
    }

    @Override
    public void clear(String conversationId) {
        this.conversationHistory.remove(conversationId);
    }

    private void summarizeHistory(String conversationId) {
        List<Message> allMessages = conversationHistory.get(conversationId);

        int size = allMessages.size();
        if (size > configProperties.maxDetailConversationMessagesHistory) {
            int indexOfOldMessage = size - configProperties.maxDetailConversationMessagesHistory - 1;
            Message oldDetailledMessage = allMessages.get(indexOfOldMessage);
            Message summarizedMessage = summarizerChatMemoryClient.summarizeOldMessage(oldDetailledMessage);
            allMessages.set(indexOfOldMessage, summarizedMessage);
        }

        if (size > configProperties.maxConversationMessageHistory) {
            Message summaryMessage = allMessages.get(0);
            Message messageToAddToSummary = allMessages.get(1);
            Message mergedSummaryMessage = summarizerChatMemoryClient.createOverallSummaryMessage(summaryMessage, messageToAddToSummary);

            allMessages.set(0, mergedSummaryMessage);
            allMessages.remove(1);
        }
    }
}
