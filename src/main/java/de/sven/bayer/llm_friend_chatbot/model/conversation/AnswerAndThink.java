package de.sven.bayer.llm_friend_chatbot.model.conversation;

public record AnswerAndThink(String answer, String think) {

    @Override
    public String toString() {
        return answer;
    }
}
