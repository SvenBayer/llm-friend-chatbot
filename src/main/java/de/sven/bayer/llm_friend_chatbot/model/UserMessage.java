package de.sven.bayer.llm_friend_chatbot.model;

public record UserMessage(String message) {

    @Override
    public String toString() {
        return message;
    }
}
