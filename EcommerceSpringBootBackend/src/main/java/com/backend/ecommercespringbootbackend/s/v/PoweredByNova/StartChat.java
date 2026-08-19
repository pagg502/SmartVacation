package com.backend.ecommercespringbootbackend.s.v.PoweredByNova;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.time.LocalDateTime;

@Service
public class StartChat {

    public String startChat(String message, ChatHistory history) {
        NovaApiService apiService = new NovaApiService();

        //Time
        String currentDate = LocalDate.now().format(DateTimeFormatter.ISO_DATE); //"2026-08-18"

        String systemPrompt = """
    Current date: %s.
    You are a travel assistant AI.
    
    CORE RULES:
    1. STRICT JSON: Return ONLY a valid JSON object. No text before or after.
    2. ESCAPING: Use \\\\n for newlines in 'chat_response'. NEVER use literal line breaks.
    3. DEFAULTS: Use 0 for unknown integers in 'party_size'. Use "" (empty string) for unknown strings.
    4. ARRAYS: 'missing_fields' must ALWAYS be an array. Never return null.\s
       If no fields are missing, return [].
    5. DATE CHECK — REQUIRED:
          - Current date is %1$s. Compare travel_start_date against current_date.
          - IF travel_start_date < current_date: You MUST reject the date. Set 'chat_response' to inform the user that the date is in the past and they must choose a future date. Do NOT ask for destination/departure yet until a valid future date is provided.
          - IF travel_start_date >= current_date: Accept the date and proceed to collect missing fields.
    
    CONVERSATION LOGIC:
    6. PHASE 1 (Data Collection): Collect destination, departure_city, date_range, and party_size.
       - ANCHORING: Once 'destination' is set, do not change it unless the user says "Change my destination". 
       - ASSUMPTION: If a new city is mentioned while 'destination' is already filled, assign it to 'departure_city'.
    7. PHASE 2 (Tool Execution): Once destination, departure_city, date_range, and party_size are all present:
       - You MUST populate 'callTools' with 'get_weather', 'find_flights', and 'find_hotels'.
       - Do NOT ask for name/email in this phase.
    8. PHASE 3 (Reporting & Booking): After receiving tool results (provided via System Update):
       - Generate a Markdown report in 'chat_response'.
       - Ask if they want to book. If YES, only then collect 'name' and 'email'.
       - Once name/email are present, call 'send_summary'.

    JSON SCHEMA:
    {
      "chat_response": "...",
      "extracted_data": {
        "intent": "collecting | tool_trigger | reporting | booking | exited",
        "name": "", "email": "",
        "destination": { "raw": "", "normalized": "" },
        "departure_city": { "raw": "", "normalized": "" },
        "date_range": { "raw": "", "start": "", "end": "", "is_flexible": false },
        "party_size": { "adults": 0, "children": 0, "infants": 0 },
        "missing_fields": [],
        "callTools": {} 
      }
    }

    TOOLS:
    - get_weather: { "city", "start_date", "end_date" }
    - find_flights: { "departure_city", "destination", "start_date" }
    - find_hotels: { "destination", "check_in", "check_out" }
    - send_summary: { "name", "email" }
    """.formatted(currentDate);

        String currentInput = message;
        boolean hasWeather = false, hasFlights = false, hasHotels = false, sentSummary = false;

        try {
            // 1. Force the JSON instruction
            String inputWithInstruction = currentInput +
                    "\n\nIMPORTANT: You must respond ONLY with the JSON object. Do not include conversational filler outside the JSON.";
            if (!currentInput.equals("Start conversation") && !currentInput.startsWith("System Update")) {
                inputWithInstruction = currentInput + "\n(Strict Reminder: Return ONLY valid JSON with chat_response and extracted_data)";
            }

            // 2. Call AI (One turn)
            StartChatResponse result = apiService.getChatCompletion(systemPrompt, inputWithInstruction, history);

            if (result != null && result.getExtractedData() != null) {

                String intent = result.getExtractedData().getIntent();
                if ("exited".equalsIgnoreCase(intent)) {
                    String closingResponse = result.getChatResponse();
                    history.addTurn(message, closingResponse);
                    return closingResponse;
                }
                List<String> missing = result.getExtractedData().getMissingFields();

                // 3. PHASE: DATA COMPLETE? -> TRIGGER TOOLS
                if (missing != null && missing.isEmpty() && !currentInput.equals("Start conversation")) {
                    var toolsRequested = result.getExtractedData().getCallTools();

                    if (toolsRequested != null && !toolsRequested.isEmpty()) {
                        StringBuilder toolResults = new StringBuilder("Tool Results: ");
                        ToolsToCall toolExecutor = new ToolsToCall();

                        for (var entry : toolsRequested.entrySet()) {
                            String toolName = entry.getKey();
                            Map<String, Object> params = entry.getValue();
                            String output = "";

                            try {
                                switch (toolName) {
                                    case "get_weather" -> {
                                        output = toolExecutor.fetchWeather(params);
                                        if (output != null && !output.contains("Error")) hasWeather = true;
                                    }
                                    case "find_flights" -> {
                                        output = toolExecutor.searchFlights(params);
                                        if (output != null && !output.contains("Error")) hasFlights = true;
                                    }
                                    case "find_hotels" -> {
                                        output = toolExecutor.searchHotels(params);
                                        if (output != null && !output.contains("Error")) hasHotels = true;
                                    }
                                    case "send_summary" -> {
                                        output = toolExecutor.sendSummary(params);
                                        if (output != null && !output.contains("Error")) sentSummary = true;
                                    }
                                }
                            } catch (Exception e) {
                                output = "Error: " + e.getMessage();
                            }
                            toolResults.append("[").append(toolName).append(": ").append(output).append("] ");
                        }

                        // 4. PREPARE NEXT INPUT FOR AI BASED ON TOOLS
                        String nextInput;
                        if (hasWeather && hasFlights && hasHotels) {
                            systemPrompt = getReportingPrompt();
                            nextInput = "System Update: All data retrieved. " + toolResults + " Generate the report.";
                        } else if (sentSummary) {
                            nextInput = "System Update: Trip successfully booked and summary email sent! Enthusiastically confirm that the trip is booked and booked successfully, mention the confirmation email, and ask if they need anything else.";
                        } else {
                            nextInput = "System Update: Tool data received: " + toolResults;
                        }

                        // 5. CALL AI AGAIN TO GET FINAL RESPONSE AFTER TOOLS
                        StartChatResponse finalResult = apiService.getChatCompletion(systemPrompt, nextInput, history);

                        // NEW: Update history with final tool-enriched response
                        String finalResponse = (finalResult != null) ? finalResult.getChatResponse() : "Error processing tool report.";
                        history.addTurn(message, finalResponse);
                        return finalResponse;
                    }
                }

                // NEW: Update history for standard data collection turns
                String standardResponse = result.getChatResponse();
                history.addTurn(message, standardResponse);
                return standardResponse;

            } else {
                return (result != null) ? result.getChatResponse() : "Connection error. Please try again.";
            }

        } catch (Exception e) {
            System.err.println("Error in StartChat processing: " + e.getMessage());
            return "Error: System failed to process the request.";
        }
    }

    public String getReportingPrompt(){
        return """
        You are now in 'Reporting & Booking' mode.
        
        GOALS:
        1. Create a beautiful, Markdown-formatted trip report using the data provided in 'System Update'.
        2. Format: Use ## for the destination title and * for details.
        3. At the end, ask: "Would you like me to book this trip for you?"
        
        IF THE CUSTOMER SAYS YES:
        1. Politely ask for their First Name and Email.
        2. Set 'intent' to 'booking' in the JSON.
        3. Populate 'name' and 'email' fields in the JSON once provided.
        4. Once the user provides their name and email, you MUST call the send_summary tool with those details to finalize the process.
        5. STRICT RULE: Your ENTIRE response must be a single JSON object.\s
           Put the Markdown report INSIDE the "chat_response" field.\s
           Use \\\\n for all newlines in the report.
        6. EXIT HANDLING: If the summary email has been sent and the user responds with a farewell, rejection, or confirmation like "nope thanks", "no", or "goodbye", set intent to 'exited', do NOT trigger any tools, and provide a warm closing message wishing them a great trip without mentioning the summary email again.
        
        STRICT JSON SCHEMA:
        {
          "chat_response": "## Trip Report\\\\n* Destination: Miami...",
          "extracted_data": {
            "name": "string or null",
            "email": "string or null",
            "intent": "reporting | booking | exited",
            "destination": { "raw": "...", "normalized": "..." },
            "missing_fields": [],
            "callTools": {}
          }
        }
        """;
    }
}