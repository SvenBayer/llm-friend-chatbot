package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.MessageType;
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

    @Value("classpath:templates/summarization-prompt.st")
    private Resource summarizationPromptTemplate;

    @Value("classpath:templates/overall-summary-prompt.st")
    private Resource overallSummaryPromptTemplate;

    public SystemPromptService(ConfigProperties configProperties) {
        this.configProperties = configProperties;
    }

    public Prompt getInitialSystemPrompt() {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        return createPromptFromTemplate(this.initialSystemPromptTemplate, params);
    }

    public Prompt getSystemPrompt() {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        return createPromptFromTemplate(this.systemPromptTemplate, params);
    }

    public Prompt getSummarizationPrompt(MessageType speaker, Message oldDetailledMessage) {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        params.put("maxLength", configProperties.maxSummarizationLength);
        params.put("speaker", speaker.getValue());
        params.put("detailedConversation", oldDetailledMessage.getText());
        return createPromptFromTemplate(this.summarizationPromptTemplate, params);
    }

    public Prompt getOverallSummaryPrompt(MessageType speaker, Message existingSummary, Message oldDetailledMessage) {
        Map<String, Object> params = new HashMap<>();
        params.put("llmName", configProperties.llmName);
        params.put("maxLength", configProperties.maxSummarizationLength);
        params.put("speaker", speaker.getValue());
        params.put("existingSummary", existingSummary.getText());
        params.put("detailedConversation", oldDetailledMessage.getText());
        return createPromptFromTemplate(this.overallSummaryPromptTemplate, params);
    }

    private Prompt createPromptFromTemplate(Resource templateResource, Map<String, Object> params) {
        PromptTemplate promptTemplate = new PromptTemplate(templateResource);
        return promptTemplate.create(params);
    }
}
