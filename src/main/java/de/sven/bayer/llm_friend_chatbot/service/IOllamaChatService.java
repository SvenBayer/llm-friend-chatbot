package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.model.conversation.LlmAnswerWithThink;
import de.sven.bayer.llm_friend_chatbot.model.conversation.MessageFromUser;

public interface IOllamaChatService {

    LlmAnswerWithThink responseForMessage(MessageFromUser messageFromUser);
}
