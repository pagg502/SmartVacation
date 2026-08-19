package com.backend.ecommercespringbootbackend.s.v.PoweredByNova;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//The Entities (StartChatResponse.java)
//This structure mirrors your JSON schema exactly. Using @JsonIgnoreProperties ensures that if the AI adds extra fields, the code won't crash.

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class StartChatResponse {
    @JsonProperty("chat_response")
    private String chatResponse;

    @JsonProperty("extracted_data")
    private ExtractedData extractedData;

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ExtractedData {
        private String intent;
        private String name;
        private String email;
        private FieldDetail destination;
        @JsonProperty("departure_city")
        private FieldDetail departureCity;
        @JsonProperty("date_range")
        private DateRange dateRange;
        @JsonProperty("party_size")
        private PartySize partySize;
        @JsonProperty("missing_fields")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        private List<String> missingFields = new ArrayList<>();
        private double confidence;
        @JsonProperty("callTools")
        private Map<String, Map<String, Object>> callTools = new HashMap<>();
    }

    @Data
    public static class ToolRequest {
        private Map<String, Object> params;
    }

    @Data
    public static class FieldDetail {
        private String raw;
        private String normalized;
    }

    @Data
    public static class DateRange {
        private String raw;
        private String start;
        private String end;
        @JsonProperty("is_flexible")
        private boolean isFlexible;
    }

    @Data
    public static class PartySize {
        private int adults = 0;
        private int children = 0;
        private int infants = 0;
    }
}
