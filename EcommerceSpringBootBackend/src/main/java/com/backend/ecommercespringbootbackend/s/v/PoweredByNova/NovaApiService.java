package com.backend.ecommercespringbootbackend.s.v.PoweredByNova;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClientAsync;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionSystemMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class NovaApiService {
    private final OpenAIClientAsync client;
    private final ObjectMapper objectMapper;


    public NovaApiService() {
        this.client = OpenAIOkHttpClient.builder()
                .baseUrl("https://api.nova.amazon.com/v1")
                .apiKey(System.getenv("NOVA_API_KEY"))
                .build()
                .async();
        this.objectMapper = new ObjectMapper();
    }

    public StartChatResponse getChatCompletion(String systemPrompt, String userInput, ChatHistory history) {
        List<ChatCompletionMessageParam> messages = new ArrayList<>();

        // 1. ALWAYS start with the System Prompt to keep the AI in "JSON mode"
        messages.add(ChatCompletionMessageParam.ofSystem(
                ChatCompletionSystemMessageParam.builder().content(systemPrompt).build()
        ));

        // 2. Add history (ensure history doesn't contain the system prompt again)
        if (history != null) {
            messages.addAll(history.getMessages());
        }

        // 3. Add current User Input
        messages.add(ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder().content(userInput).build()
        ));

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .model("nova-2-lite-v1")
                .messages(messages)
                .temperature(0.3) // LOWER temperature (0.1 - 0.3) makes AI more likely to follow JSON rules
                .build();

        try {
            ChatCompletion response = client.chat().completions().create(params).join();
            String rawContent = response.choices().get(0).message().content().orElse("");

            int firstBrace = rawContent.indexOf("{");
            int lastBrace = rawContent.lastIndexOf("}");

            if (firstBrace == -1 || lastBrace == -1) {
                // AI failed JSON. Let's wrap its text into a valid object so the rest of the app doesn't break.
                StartChatResponse fallback = new StartChatResponse();
                fallback.setChatResponse(rawContent);

                StartChatResponse.ExtractedData data = new StartChatResponse.ExtractedData();
                data.setMissingFields(new ArrayList<>()); // Tell the app nothing is missing so it doesn't loop
                data.setIntent("collecting");
                fallback.setExtractedData(data);

                return fallback;
            }

            String jsonOnly = rawContent.substring(firstBrace, lastBrace + 1);
            return objectMapper.readValue(jsonOnly, StartChatResponse.class);

        } catch (Exception e) {
            System.err.println("API Error: " + e.getMessage());
            return null;
        }
    }
}