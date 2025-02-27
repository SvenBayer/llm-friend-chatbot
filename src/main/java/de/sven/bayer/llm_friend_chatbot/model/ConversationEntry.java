package de.sven.bayer.llm_friend_chatbot.model;

import de.sven.bayer.llm_friend_chatbot.model.conversation.AnswerAndThink;
import de.sven.bayer.llm_friend_chatbot.model.conversation.MessageFromUser;

public record ConversationEntry(MessageFromUser messageFromUser, AnswerAndThink answerAndThink) {
}
