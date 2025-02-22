package de.sven.bayer.llm_friend_chatbot;

import de.sven.bayer.llm_friend_chatbot.config.ConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties(ConfigProperties.class)
public class LlmFriendChatbotApplication {

	public static void main(String[] args) {
		SpringApplication.run(LlmFriendChatbotApplication.class, args);
	}

}
