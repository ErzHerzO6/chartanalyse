package com.chartanalyse;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import java.util.Calendar;
import java.util.List;

public class App {
    public static void main(String[] args) {
        try {
            System.out.println("=== S&P 500 Daten von Yahoo Finance ===\n");
            
            // 1. Aktuelle Daten holen
            Stock stock = YahooFinance.get("^GSPC"); // S&P 500 Index
            
            System.out.println("Symbol: " + stock.getSymbol());
            System.out.println("Name: " + stock.getName());
            System.out.println("Aktueller Preis: $" + stock.getQuote().getPrice());
            System.out.println("Tagesaenderung: " + stock.getQuote().getChangeInPercent() + "%");
            System.out.println("Volumen: " + stock.getQuote().getVolume());
            System.out.println("Tageshoch: $" + stock.getQuote().getDayHigh());
            System.out.println("Tagestief: $" + stock.getQuote().getDayLow());
            
            // 2. Historische Daten holen (letzte 30 Tage)
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.DAY_OF_MONTH, -30);
            
            List<HistoricalQuote> history = stock.getHistory(from, to, Interval.DAILY);
            
            System.out.println("\n=== Historische Daten (letzte 30 Tage) ===");
            System.out.println("Anzahl Datenpunkte: " + history.size());
            System.out.println("\nErste 5 Tage:");
            
            for (int i = 0; i < Math.min(5, history.size()); i++) {
                HistoricalQuote quote = history.get(i);
                System.out.println(
                    quote.getDate().getTime() + " | " +
                    "Open: $" + quote.getOpen() + " | " +
                    "High: $" + quote.getHigh() + " | " +
                    "Low: $" + quote.getLow() + " | " +
                    "Close: $" + quote.getClose() + " | " +
                    "Volume: " + quote.getVolume()
                );
            }
            
            // 3. Einfache Datenverarbeitung: Durchschnittspreis berechnen
            double avgClose = history.stream()
                .mapToDouble(q -> q.getClose().doubleValue())
                .average()
                .orElse(0.0);
            
            System.out.println("\nDurchschnittlicher Schlusskurs (30 Tage): $" + 
                String.format("%.2f", avgClose));
            
            // 4. Hoechster und niedrigster Preis
            double maxPrice = history.stream()
                .mapToDouble(q -> q.getHigh().doubleValue())
                .max()
                .orElse(0.0);
            
            double minPrice = history.stream()
                .mapToDouble(q -> q.getLow().doubleValue())
                .min()
                .orElse(0.0);
            
            System.out.println("Hoechster Preis (30 Tage): $" + String.format("%.2f", maxPrice));
            System.out.println("Niedrigster Preis (30 Tage): $" + String.format("%.2f", minPrice));
            
        } catch (Exception e) {
            System.err.println("Fehler beim Abrufen der Daten: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
