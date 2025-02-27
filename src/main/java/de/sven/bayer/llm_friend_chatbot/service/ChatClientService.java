package de.sven.bayer.llm_friend_chatbot.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.PromptChatMemoryAdvisor;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Service;

@Service
public class ChatClientService implements ChatClient {

    private final ChatClient chatClient;

    public ChatClientService(ChatClient.Builder chatClientBuilder, SummarizingChatMemoryService summarizingChatMemoryService) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new PromptChatMemoryAdvisor(summarizingChatMemoryService))
                .build();
    }

    @Override
    public ChatClientRequestSpec prompt() {
        return chatClient.prompt();
    }

    @Override
    public ChatClientRequestSpec prompt(String content) {
        return chatClient.prompt(content);
    }

    @Override
    public ChatClientRequestSpec prompt(Prompt prompt) {
        return chatClient.prompt(prompt);
    }

    @Override
    public Builder mutate() {
        return chatClient.mutate();
    }
}
