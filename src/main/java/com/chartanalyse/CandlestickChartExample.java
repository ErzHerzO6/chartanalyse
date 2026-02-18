package com.chartanalyse;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import org.knowm.xchart.OHLCChart;
import org.knowm.xchart.OHLCChartBuilder;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.style.Styler;

import java.text.SimpleDateFormat;
import java.util.*;

public class CandlestickChartExample {
    public static void main(String[] args) {
        try {
            System.out.println("Lade Aktiendaten...");
            
            // Daten von Yahoo Finance holen
            Stock stock = YahooFinance.get("AAPL");
            
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.MONTH, -3); // Letzte 3 Monate
            
            List<HistoricalQuote> history = stock.getHistory(from, to, Interval.DAILY);
            Collections.reverse(history); // Älteste zuerst
            
            // Daten für OHLC Chart vorbereiten
            List<Date> dates = new ArrayList<>();
            List<Double> openData = new ArrayList<>();
            List<Double> highData = new ArrayList<>();
            List<Double> lowData = new ArrayList<>();
            List<Double> closeData = new ArrayList<>();
            
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            
            for (HistoricalQuote quote : history) {
                dates.add(quote.getDate().getTime());
                openData.add(quote.getOpen().doubleValue());
                highData.add(quote.getHigh().doubleValue());
                lowData.add(quote.getLow().doubleValue());
                closeData.add(quote.getClose().doubleValue());
            }
            
            System.out.println("Erstelle Candlestick Chart mit " + history.size() + " Datenpunkten...");
            
            // OHLC Chart erstellen
            OHLCChart chart = new OHLCChartBuilder()
                .width(1400)
                .height(700)
                .title(stock.getName() + " (" + stock.getSymbol() + ") - Candlestick Chart")
                .xAxisTitle("Datum")
                .yAxisTitle("Preis (USD)")
                .build();
            
            // Chart Style
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            
            // OHLC Serie hinzufügen
            chart.addSeries(
                stock.getSymbol(),
                dates,
                openData,
                highData,
                lowData,
                closeData
            );
            
            // Chart anzeigen
            System.out.println("Zeige Chart...");
            new SwingWrapper<>(chart).displayChart();
            
            // Statistiken ausgeben
            double avgClose = closeData.stream().mapToDouble(d -> d).average().orElse(0.0);
            double maxPrice = highData.stream().mapToDouble(d -> d).max().orElse(0.0);
            double minPrice = lowData.stream().mapToDouble(d -> d).min().orElse(0.0);
            
            System.out.println("\n=== Statistiken ===");
            System.out.println("Durchschnittspreis: $" + String.format("%.2f", avgClose));
            System.out.println("Höchster Preis: $" + String.format("%.2f", maxPrice));
            System.out.println("Niedrigster Preis: $" + String.format("%.2f", minPrice));
            System.out.println("Preisänderung: $" + String.format("%.2f", closeData.get(closeData.size()-1) - closeData.get(0)));
            
        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
