package com.backend.ecommercespringbootbackend.s.v.PoweredByNova;

import com.openai.models.chat.completions.ChatCompletionAssistantMessageParam;
import com.openai.models.chat.completions.ChatCompletionMessageParam;
import com.openai.models.chat.completions.ChatCompletionUserMessageParam;
import lombok.Getter;
import java.util.ArrayList;
import java.util.List;

public class ChatHistory {
    @Getter
    private final List<ChatCompletionMessageParam> messages = new ArrayList<>();

    public void addTurn(String userInput, String aiResponseJson) {
        // Add User message
        messages.add(ChatCompletionMessageParam.ofUser(
                ChatCompletionUserMessageParam.builder()
                        .content(userInput)
                        .build()
        ));

        // Add Assistant message
        messages.add(ChatCompletionMessageParam.ofAssistant(
                ChatCompletionAssistantMessageParam.builder()
                        .content(aiResponseJson)
                        .build()
        ));
    }
}
