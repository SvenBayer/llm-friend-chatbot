package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.ai.tool.LlmMemoryTool;
import de.sven.bayer.llm_friend_chatbot.model.conversation.AnswerAndThink;
import de.sven.bayer.llm_friend_chatbot.model.conversation.LlmAnswerWithThink;
import de.sven.bayer.llm_friend_chatbot.model.conversation.MessageFromUser;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Service
public class OllamaChatService implements IOllamaChatService {

    private final OllamaProcessService ollamaProcessService;
    private final ChatClient chatClient;
    private final SystemPromptService systemPromptService;
    private final ThinkAnswerExtractorService thinkAnswerExtractorService;

    public OllamaChatService(ChatClient.Builder chatClient, SummarizingChatMemoryService summarizingChatMemoryService,OllamaProcessService ollamaProcessService, SystemPromptService systemPromptService, ThinkAnswerExtractorService thinkAnswerExtractorService) {
        this.chatClient = chatClient.defaultAdvisors(
                new PromptChatMemoryAdvisor(summarizingChatMemoryService)
        ).build();
        this.ollamaProcessService = ollamaProcessService;
        this.systemPromptService = systemPromptService;
        this.thinkAnswerExtractorService = thinkAnswerExtractorService;
    }

    public LlmAnswerWithThink responseForMessage(MessageFromUser messageFromUser) {
        Prompt systemPrompt;
        if (messageFromUser.getConversationId() == null || messageFromUser.getConversationId().isEmpty()) {
            messageFromUser.setConversationId(UUID.randomUUID().toString());
            systemPrompt = systemPromptService.getInitialSystemPrompt();
        } else {
            systemPrompt = systemPromptService.getSystemPrompt();
        }
        System.out.printf("System prompt: " + systemPrompt.getContents());

        String response = chatClient.prompt()
                .system(systemPrompt.getContents())
                .user(messageFromUser.getMessage())
                //.tools(new LlmMemoryTool())
                .advisors(advisorSpec -> advisorSpec
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, messageFromUser.getConversationId())
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 100))
                .call()
                .content();
        ollamaProcessService.stopOllamaContainer();
        System.out.println("Response: " + response);

        AnswerAndThink answerAndThink = thinkAnswerExtractorService.extractAnswer(response);
        LlmAnswerWithThink llmAnswerWithThink = new LlmAnswerWithThink(answerAndThink.answer(), answerAndThink.think(), messageFromUser.getConversationId());

        return llmAnswerWithThink;
    }
}
