package de.sven.bayer.llm_friend_chatbot.service;

import de.sven.bayer.llm_friend_chatbot.model.conversation.AnswerAndThink;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ThinkAnswerExtractorService {

    public AnswerAndThink extractAnswer(String answerWithThink) {
        String processedAnswer;
        String think = null;
        if (answerWithThink.contains("</think>")) {
            Pattern pattern = Pattern.compile("<think>(.*?)</think>", Pattern.DOTALL);
            Matcher matcher = pattern.matcher(answerWithThink);
            StringBuilder thinkContent = new StringBuilder();
            String remainingText = answerWithThink;

            while (matcher.find()) {
                if (!thinkContent.isEmpty()) {
                    thinkContent.append("\n");
                }
                thinkContent.append(matcher.group(1).strip());
                remainingText = remainingText.replace(matcher.group(0), "");
            }

            think = thinkContent.toString();
            processedAnswer = remainingText.strip();
        } else {
            processedAnswer = answerWithThink;
        }

        return new AnswerAndThink(processedAnswer, think);
    }
}
