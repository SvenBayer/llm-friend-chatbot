package de.sven.bayer.llm_friend_chatbot.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "de.sven.bayer.llm.friend.chatbot")
@Data
public class ConfigProperties {

    @NotNull
    public String llmName;
}
