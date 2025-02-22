package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.model.LlmAnswer;
import de.sven.bayer.llm_friend_chatbot.model.UserMessage;

public interface IOllamaChatService {

    LlmAnswer responseForMessage(UserMessage userMessage);
}
