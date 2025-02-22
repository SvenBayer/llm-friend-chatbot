package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.model.LlmAnswer;
import de.sven.bayer.llm_friend_chatbot.model.LlmAnswerWithThink;
import org.springframework.stereotype.Service;

@Service
public class ThinkAnswerExtractorService {

    public LlmAnswer extractAnswer(LlmAnswerWithThink llmAnswerWithThink) {
        String answerWithThink = llmAnswerWithThink.answerWithThink();
        String processedAnswer = answerWithThink.split("</think>", 2)[1]
                .replaceAll("[\\p{So}\\p{Cn}]", "")
                .strip();
        return new LlmAnswer(processedAnswer);
    }
}
