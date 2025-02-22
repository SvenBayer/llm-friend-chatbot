package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import de.sven.bayer.llm_friend_chatbot.model.ConversationHistory;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemPromptService {

    private final ConfigProperties configProperties;

    private final ConversationHistory conversationHistory;

    @Value("classpath:templates/initial-system-prompt.st")
    private Resource initialSystemPromptTemplate;

    @Value("classpath:templates/system-prompt.st")
    private Resource systemPromptTemplate;

    public SystemPromptService(ConfigProperties configProperties, ConversationHistory conversationHistory) {
        this.configProperties = configProperties;
        this.conversationHistory = conversationHistory;
    }

    public Prompt getSystemPrompt() {
        String llmName = configProperties.llmName;
        int conversationSize = conversationHistory.getConversationHistory().size();
        PromptTemplate systemPromptTemplate;
        if (conversationSize == 0) {
            systemPromptTemplate = new PromptTemplate(this.initialSystemPromptTemplate);
            Map<String, Object> replaceParams = new HashMap<>();
            replaceParams.put("llmName", llmName);
            replaceParams.put("llmNameUppercase", llmName.toUpperCase());
            return systemPromptTemplate.create(replaceParams);
        } else {
            systemPromptTemplate = new PromptTemplate(this.systemPromptTemplate);
            Map<String, Object> replaceParams = new HashMap<>();
            replaceParams.put("llmName", llmName);
            replaceParams.put("llmNameUppercase", llmName.toUpperCase());
            replaceParams.put("conversationHistory", conversationHistory);
            return systemPromptTemplate.create(replaceParams);
        }
    }
}
