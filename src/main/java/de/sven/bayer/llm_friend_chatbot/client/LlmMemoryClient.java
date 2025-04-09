package de.sven.bayer.llm_friend_chatbot.client;

import de.sven.bayer.llm_friend_chatbot.model.conversation.MessageFromUser;
import de.sven.bayer.llm_friend_chatbot.model.message.RelevantMemories;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class LlmMemoryClient {

    private static final String SUMMARIZER_BASE_URL = "http://localhost:8078";
    private final RestTemplate restTemplate = new RestTemplate();

    public RelevantMemories memorizeMessage(MessageFromUser messageFromUser) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            String request = messageFromUser.getMessage();

            HttpEntity<String> httpEntity = new HttpEntity<>(request, headers);

            ResponseEntity<RelevantMemories> response = restTemplate.exchange(
                    SUMMARIZER_BASE_URL + "/memorizeMessage",
                    HttpMethod.POST,
                    httpEntity,
                    RelevantMemories.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error calling LLM Memory service", e);
            return null;
        }
    }

    public String findInformation(String description) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<String> httpEntity = new HttpEntity<>(description, headers);

            ResponseEntity<String> response = restTemplate.exchange(
                    SUMMARIZER_BASE_URL + "/findInformation",
                    HttpMethod.POST,
                    httpEntity,
                    String.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            log.error("Error calling LLM Memory service", e);
            return null;
        }
    }
}
