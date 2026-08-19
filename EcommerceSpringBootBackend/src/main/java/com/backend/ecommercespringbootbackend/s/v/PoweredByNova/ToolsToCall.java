package com.backend.ecommercespringbootbackend.s.v.PoweredByNova;

import java.util.Map;

public class ToolsToCall {
    public String fetchWeather(Map<String, Object> params) {
        try {
            // Assume this is your real API call
            //String response = callRealWeatherApi(params);

            //if (response == null || response.isEmpty()) return null;
            // Real API logic goes here. For now, we return a string.
            return "Avg Temp: 75°F, Sunny. 5-year data shows perfect conditions.";
        } catch (Exception e) {
            System.out.println("Error wile executing fetchWeather: " + e.getMessage());
            return "Error wile executing fetchWeather: " + e.getMessage();
        }
    }



    public String searchFlights(Map<String, Object> params) {
        try{
            return "Found flights from " + params.get("departure_city") + " starting at $450.";
        }catch(Exception e){
            System.out.println("Error wile executing searchFlights: " + e.getMessage());
            return "Error wile executing searchFlights: " + e.getMessage();
        }
    }

    public String searchHotels(Map<String, Object> params) {
        try{
            return "Top rated hotels in " + params.get("destination") + " available from $120/night.";
        }catch(Exception e){
            System.out.println("Error wile executing searchHotels: " + e.getMessage());
            return "Error wile executing searchHotels: " + e.getMessage();
        }

    }

    public String sendSummary(Map<String, Object> params) {
        try{
            return "Summary sent " + params.get("destination") + " Thanks for booking with us!";
        }catch(Exception e){
            System.out.println("Error wile executing sendSummary: " + e.getMessage());
            return "Error wile executing sendSummary: " + e.getMessage();
        }

    }
}

