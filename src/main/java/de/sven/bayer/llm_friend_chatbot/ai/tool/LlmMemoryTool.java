package de.sven.bayer.llm_friend_chatbot.ai.tool;

import de.sven.bayer.llm_friend_chatbot.client.LlmMemoryClient;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LlmMemoryTool {

    private final LlmMemoryClient llmMemoryClient;

    public LlmMemoryTool(LlmMemoryClient llmMemoryClient) {
        this.llmMemoryClient = llmMemoryClient;
    }

    @Tool(description = "Get information from your memory from the past about things that you talked about with the user")
    String getInformationAboutUser(@ToolParam(description = "a description of want you want to retrieve information about from your conversation memory") String description) {
        System.out.println("\n\nDescription: " + description);
        String information = llmMemoryClient.findInformation(description);
        System.out.println("Information: " + information);
        if (information != null && !information.isEmpty()) {
            return information;
        } else {
            return "I don't have any information about that.";
        }
    }
}
