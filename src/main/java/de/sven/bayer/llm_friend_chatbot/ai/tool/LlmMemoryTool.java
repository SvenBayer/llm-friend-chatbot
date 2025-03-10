package de.sven.bayer.llm_friend_chatbot.ai.tool;

import org.springframework.ai.document.Document;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LlmMemoryTool {

    private final VectorStore vectorStore;

    public LlmMemoryTool(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    @Tool(description = "Get information from your memory from the past about things that you talked about with the user")
    String getInformationAboutUser(@ToolParam(description = "a description of want you want to retrieve information about from your conversation memory") String description) {
        List<Document> conversationId = vectorStore.similaritySearch(SearchRequest.builder()
                .topK(100)
                .filterExpression(new FilterExpressionBuilder().in("conversationId", List.of("36886b53-6ca0-4672-8896-463636a5172f"))
                        .build())
                .build());
        for (Document doc : conversationId) {
            System.out.println("ID SEARCH: " + doc.getText());
        }
        // Retrieve similar documents from the vector store
        System.out.println("Searching documents for description: " + description);
        List<Document> similarDocuments = vectorStore.similaritySearch(SearchRequest.builder().query(description).topK(10).build());

        StringBuilder result = new StringBuilder();
        // Process the retrieved documents as needed
        if (similarDocuments != null && !similarDocuments.isEmpty()) {
            similarDocuments.forEach(doc -> {
                result.append(doc.getText() + "\n");
                //result.append(doc.getFormattedContent() + "\n");
                System.out.println("Retrieved Document: " + doc.getText());
            });
        }
        return result.toString();
    }
}
