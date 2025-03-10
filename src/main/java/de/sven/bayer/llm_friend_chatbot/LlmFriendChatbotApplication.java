package de.sven.bayer.llm_friend_chatbot;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.ollama.management.ModelManagementOptions;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class LlmFriendChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmFriendChatbotApplication.class, args);
	}

}
