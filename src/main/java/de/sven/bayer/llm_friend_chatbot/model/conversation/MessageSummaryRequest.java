package de.sven.bayer.llm_friend_chatbot.model.conversation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageSummaryRequest {
    @NotNull
    private MessageWithType summaryMessage;

    @NotNull
    private MessageWithType messageToAddToSummary;
}