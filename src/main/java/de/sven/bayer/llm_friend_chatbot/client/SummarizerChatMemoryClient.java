package de.sven.bayer.llm_friend_chatbot.client;

import de.sven.bayer.llm_friend_chatbot.model.conversation.MessageSummaryRequest;
import de.sven.bayer.llm_friend_chatbot.model.conversation.MessageWithType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.messages.*;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class SummarizerChatMemoryClient {

    private static final String SUMMARIZER_BASE_URL = "http://localhost:8079";
    private final RestTemplate restTemplate = new RestTemplate();

    public Message summarizeOldMessage(Message oldDetailledMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String text = oldDetailledMessage.getText();
            MessageType messageType = oldDetailledMessage.getMessageType();

            MessageWithType request = new MessageWithType(text, messageType);

            HttpEntity<MessageWithType> httpEntity = new HttpEntity<>(request, headers);

            ResponseEntity<MessageWithType> response = restTemplate.exchange(
                    SUMMARIZER_BASE_URL + "/summarizeOldMessage",
                    HttpMethod.POST,
                    httpEntity,
                    MessageWithType.class
            );
            return MessageWithType.createMessage(response.getBody());
        } catch (RestClientException e) {
            log.error("Error calling Chat Summary service", e);
            return oldDetailledMessage;
        }
    }

    public Message createOverallSummaryMessage(Message summaryMessage, Message messageToAddToSummary) {
        MessageWithType summaryMessageWithType = MessageWithType.createMessageWithType(summaryMessage);
        MessageWithType messageToAddToSummaryWithType = MessageWithType.createMessageWithType(messageToAddToSummary);
        MessageSummaryRequest messageSummaryRequest = new MessageSummaryRequest(summaryMessageWithType, messageToAddToSummaryWithType);

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<MessageSummaryRequest> request = new HttpEntity<>(messageSummaryRequest, headers);

            ResponseEntity<MessageWithType> response = restTemplate.exchange(
                    SUMMARIZER_BASE_URL + "/createOverallSummaryMessage",
                    HttpMethod.POST,
                    request,
                    MessageWithType.class
            );

            return new SystemMessage(response.getBody().getText());
        } catch (RestClientException e) {
            log.error("Error calling Chat Summary service", e);
            return summaryMessage;
        }
    }
}
