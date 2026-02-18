package com.chartanalyse;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AlphaVantageExample {
    public static void main(String[] args) {
        try {
            // API Key aus config.properties laden
            String apiKey = ConfigLoader.getApiKey();
            
            if (apiKey == null || apiKey.equals("DEIN_API_KEY_HIER")) {
                System.err.println("Bitte setze deinen API Key in config.properties!");
                return;
            }
            
            System.out.println("=== Alpha Vantage - S&P 500 Daten ===\n");
            
            // S&P 500 Daten abrufen (Symbol: SPY für S&P 500 ETF)
            String symbol = "SPY";
            String url = String.format(
                "https://www.alphavantage.co/query?function=TIME_SERIES_DAILY&symbol=%s&apikey=%s",
                symbol, apiKey
            );
            
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                .url(url)
                .build();
            
            Response response = client.newCall(request).execute();
            String jsonData = response.body().string();
            
            // JSON parsen
            JsonObject jsonObject = JsonParser.parseString(jsonData).getAsJsonObject();
            
            // Metadaten anzeigen
            if (jsonObject.has("Meta Data")) {
                JsonObject metaData = jsonObject.getAsJsonObject("Meta Data");
                System.out.println("Symbol: " + metaData.get("2. Symbol").getAsString());
                System.out.println("Letztes Update: " + metaData.get("3. Last Refreshed").getAsString());
                System.out.println();
            }
            
            // Zeitreihen-Daten anzeigen
            if (jsonObject.has("Time Series (Daily)")) {
                JsonObject timeSeries = jsonObject.getAsJsonObject("Time Series (Daily)");
                System.out.println("Letzte 5 Handelstage:");
                
                int count = 0;
                for (String date : timeSeries.keySet()) {
                    if (count >= 5) break;
                    
                    JsonObject dayData = timeSeries.getAsJsonObject(date);
                    System.out.println("\nDatum: " + date);
                    System.out.println("  Open:   $" + dayData.get("1. open").getAsString());
                    System.out.println("  High:   $" + dayData.get("2. high").getAsString());
                    System.out.println("  Low:    $" + dayData.get("3. low").getAsString());
                    System.out.println("  Close:  $" + dayData.get("4. close").getAsString());
                    System.out.println("  Volume: " + dayData.get("5. volume").getAsString());
                    
                    count++;
                }
            } else if (jsonObject.has("Note")) {
                System.err.println("API Limit erreicht: " + jsonObject.get("Note").getAsString());
            } else if (jsonObject.has("Error Message")) {
                System.err.println("Fehler: " + jsonObject.get("Error Message").getAsString());
            }
            
        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
