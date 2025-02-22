package de.sven.bayer.llm_friend_chatbot.model;

public record LlmAnswer(String answer) {

    @Override
    public String toString() {
        return answer;
    }
}
