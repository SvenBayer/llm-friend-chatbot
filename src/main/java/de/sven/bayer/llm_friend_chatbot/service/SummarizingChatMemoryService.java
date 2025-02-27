package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.*;


@Service
public class SummarizingChatMemoryService implements ChatMemory {

    private final ConfigProperties configProperties;
    private final SystemPromptService systemPromptService;
    private final ChatClient summarizerChatClient;
    private final OllamaProcessService ollamaProcessService;

    private final Map<String, List<Message>> conversationHistory = new HashMap<>();

    public SummarizingChatMemoryService(ConfigProperties configProperties, SystemPromptService systemPromptService, ChatClient.Builder chatClientBuilder, OllamaProcessService ollamaProcessService) {
        this.configProperties = configProperties;
        this.systemPromptService = systemPromptService;
        this.summarizerChatClient = chatClientBuilder.build();
        this.ollamaProcessService = ollamaProcessService;
    }

    @Override
    public void add(String conversationId, List<Message> messages) {
        this.conversationHistory.putIfAbsent(conversationId, new ArrayList<>());
        ((List)this.conversationHistory.get(conversationId)).addAll(messages);

        summarizeHistory(conversationId);
    }

    @Override
    public List<Message> get(String conversationId, int lastN) {
        List<Message> all = (List)this.conversationHistory.get(conversationId);
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
            int indexOfoldMessage = size - configProperties.maxDetailConversationMessagesHistory - 1;
            Message oldDetailledMessage = allMessages.get(indexOfoldMessage);
            Message summarizedMessage = summarizeOldMessage(oldDetailledMessage);
            allMessages.set(indexOfoldMessage, summarizedMessage);
        }

        if (size > configProperties.maxConversationMessageHistory) {
            Message summaryMessage = allMessages.get(0);
            Message messageToAddToSummary = allMessages.get(1);
            summaryMessage = createOverallSummaryMessage(summaryMessage, messageToAddToSummary);

            allMessages.set(0, summaryMessage);
            allMessages.remove(1);
        }
    }

    private Message createOverallSummaryMessage(Message summaryMessage, Message messageToAddToSummary) {
        Prompt overallSummaryPrompt = systemPromptService.getOverallSummaryPrompt(messageToAddToSummary.getMessageType(), summaryMessage, messageToAddToSummary);

        String summary = summarizerChatClient.prompt()
                .user(overallSummaryPrompt.getContents())
                .call()
                .content();
        System.out.println("Overall-Summary: " + summary);

        ollamaProcessService.stopOllamaContainer();

        return new SystemMessage(summary);
    }

    private Message summarizeOldMessage(Message oldDetailledMessage) {
        MessageType oldMessageType = oldDetailledMessage.getMessageType();
        Prompt summarizationPrompt = systemPromptService.getSummarizationPrompt(oldMessageType, oldDetailledMessage);

        String summary = summarizerChatClient.prompt()
                .user(summarizationPrompt.getContents())
                .call()
                .content();
        System.out.println("Summary: " + summary);

        ollamaProcessService.stopOllamaContainer();

        return createMessageByType(oldMessageType, summary);
    }

    private Message createMessageByType(MessageType messageType, String content) {
        return switch (messageType) {
            case SYSTEM -> new SystemMessage(content);
            case USER -> new UserMessage(content);
            case ASSISTANT -> new AssistantMessage(content);
            default -> throw new IllegalArgumentException("Unknown message type: " + messageType);
        };
    }

}
