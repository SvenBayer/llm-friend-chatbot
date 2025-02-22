package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.model.ConversationHistory;
import de.sven.bayer.llm_friend_chatbot.model.LlmAnswer;
import de.sven.bayer.llm_friend_chatbot.model.LlmAnswerWithThink;
import de.sven.bayer.llm_friend_chatbot.model.UserMessage;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

@Service
public class OllamaChatService implements IOllamaChatService {

    private final OllamaProcessService ollamaProcessService;
    private final ChatClient chatClient;
    private final SystemPromptService systemPromptService;
    private final ConversationHistory conversationHistory;
    private final ThinkAnswerExtractorService thinkAnswerExtractorService;

    public OllamaChatService(OllamaProcessService ollamaProcessService, ChatClient.Builder chatClient, SystemPromptService systemPromptService, ConversationHistory conversationHistory, ThinkAnswerExtractorService thinkAnswerExtractorService) {
        this.ollamaProcessService = ollamaProcessService;
        this.chatClient = chatClient.build();
        this.systemPromptService = systemPromptService;
        this.conversationHistory = conversationHistory;
        this.thinkAnswerExtractorService = thinkAnswerExtractorService;
    }

    public LlmAnswer responseForMessage(UserMessage userMessage) {
        Prompt systemPrompt = systemPromptService.getSystemPrompt();
        System.out.printf("System prompt: " + systemPrompt.getContents());

        String response = chatClient.prompt()
                .system(systemPrompt.getContents())
                .user(userMessage.message()).messages()
                .call()
                .content();
        ollamaProcessService.stopOllamaContainer();

        LlmAnswerWithThink llmAnswerWithThink = new LlmAnswerWithThink(response);
        LlmAnswer llmAnswer = thinkAnswerExtractorService.extractAnswer(llmAnswerWithThink);

        conversationHistory.addEntry(userMessage, llmAnswer);

        return llmAnswer;
    }


}
