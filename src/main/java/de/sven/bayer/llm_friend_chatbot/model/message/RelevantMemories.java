package de.sven.bayer.llm_friend_chatbot.model.message;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
public class RelevantMemories {

    private String memoryText;
    private String assumptionText;
    private String suggestionText;
}
