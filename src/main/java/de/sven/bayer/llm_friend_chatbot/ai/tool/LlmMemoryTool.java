package de.sven.bayer.llm_friend_chatbot.ai.tool;

import org.springframework.ai.tool.annotation.Tool;

public class LlmMemoryTool {

    @Tool(description = "Get your memory from the past about things that you talked about with the user")
    String getLlmMemory() {
        return "Sven likes strawberries. The user's name is Sven. Sven wanted to make a cake. Sven likes programming.";
    }
}
