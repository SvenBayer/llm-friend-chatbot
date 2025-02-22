package de.sven.bayer.llm_friend_chatbot.controller;

import de.sven.bayer.llm_friend_chatbot.model.LlmAnswer;
import de.sven.bayer.llm_friend_chatbot.model.UserMessage;
import de.sven.bayer.llm_friend_chatbot.service.OllamaChatService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final OllamaChatService ollamaChatService;

    public ChatController(OllamaChatService ollamaChatService) {
        this.ollamaChatService = ollamaChatService;
    }

    @PostMapping("/talktoLLM")
    public LlmAnswer talkToLLM(@RequestBody UserMessage userMessage) {
        return ollamaChatService.responseForMessage(userMessage);
    }
}
