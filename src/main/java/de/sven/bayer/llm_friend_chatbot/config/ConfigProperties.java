package de.sven.bayer.llm_friend_chatbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "de.sven.bayer.llm.friend.chatbot")
@Data
public class ConfigProperties {

    /**
     * The name of the large language model (LLM) to be used in the chatbot configuration.
     * This value must not be null and is essential for identifying the specific LLM in use.
     */
    @NotNull
    public String llmName;

    /**
     * Specifies the maximum number of detailed conversation messages
     * that can be retained in the conversation history. This value is used
     * to manage the size of the conversation memory and ensure that the
     * history does not grow indefinitely.
     * Must be a non-null integer.
     */
    @NotNull
    public int maxDetailConversationMessagesHistory;

    /**
     * Specifies the maximum number of overall conversation messages (detailed and summarized) that can be retained
     * in the conversation history. This value is used to control the size of the
     * conversation history, ensuring that it remains manageable and does not
     * grow indefinitely. Must be a non-null integer.
     */
    @NotNull
    public int maxConversationMessageHistory;

    /**
     * Defines the maximum length for summarization of conversation messages.
     * This value determines the upper limit for the length of summarized content
     * generated within the conversation. It helps ensure that summarized outputs
     * remain concise and do not exceed the defined character or token limit.
     * Must be a non-null integer.
     */
    @NotNull
    public int maxSummarizationLength;
}
