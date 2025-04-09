package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import de.sven.bayer.llm_friend_chatbot.model.message.RelevantMemories;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class SystemPromptService {

    private final ConfigProperties configProperties;

    @Value("classpath:templates/initial-system-prompt.st")
    private Resource initialSystemPromptTemplate;

    @Value("classpath:templates/system-prompt.st")
    private Resource systemPromptTemplate;

    public SystemPromptService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public Prompt getInitialSystemPrompt(RelevantMemories relevantMemories) {
        Map<String, Object> params = getParamsForSystemPrompt(relevantMemories);
        return createPromptFromTemplate(this.initialSystemPromptTemplate, params);
    }

    public Prompt getSystemPrompt(RelevantMemories relevantMemories) {
        Map<String, Object> params = getParamsForSystemPrompt(relevantMemories);
        return createPromptFromTemplate(this.systemPromptTemplate, params);
    }

    private Map<String, Object> getParamsForSystemPrompt(RelevantMemories relevantMemories) {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        params.put("assumptions", relevantMemories.getAssumptionText());
        params.put("suggestions", relevantMemories.getSuggestionText());
        params.put("memories", relevantMemories.getMemoryText());
        return params;
    }

    private Prompt createPromptFromTemplate(Resource templateResource, Map<String, Object> params) {
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);
        return promptTemplate.create(params);
    }
}
