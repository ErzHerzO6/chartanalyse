package com.chartanalyse;

import yahoofinance.Stock;
import yahoofinance.YahooFinance;
import yahoofinance.histquotes.HistoricalQuote;
import yahoofinance.histquotes.Interval;

import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class ChartExample {
    public static void main(String[] args) {
        try {
            System.out.println("Lade Aktiendaten...");
            
            // Daten von Yahoo Finance holen
            Stock stock = YahooFinance.get("AAPL");
            
            Calendar from = Calendar.getInstance();
            Calendar to = Calendar.getInstance();
            from.add(Calendar.MONTH, -6); // Letzte 6 Monate
            
            List<HistoricalQuote> history = stock.getHistory(from, to, Interval.DAILY);
            Collections.reverse(history); // Älteste zuerst
            
            // Daten für Chart vorbereiten
            List<Double> xData = new ArrayList<>();
            List<Double> closeData = new ArrayList<>();
            List<Double> highData = new ArrayList<>();
            List<Double> lowData = new ArrayList<>();
            
            for (int i = 0; i < history.size(); i++) {
                HistoricalQuote quote = history.get(i);
                xData.add((double) i);
                closeData.add(quote.getClose().doubleValue());
                highData.add(quote.getHigh().doubleValue());
                lowData.add(quote.getLow().doubleValue());
            }
            
            System.out.println("Erstelle Chart mit " + history.size() + " Datenpunkten...");
            
            // Chart erstellen
            XYChart chart = new XYChartBuilder()
                .width(1200)
                .height(600)
                .title(stock.getName() + " (" + stock.getSymbol() + ") - Letzte 6 Monate")
                .xAxisTitle("Tage")
                .yAxisTitle("Preis (USD)")
                .build();
            
            // Chart Style anpassen
            chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
            chart.getStyler().setDefaultSeriesRenderStyle(org.knowm.xchart.XYSeries.XYSeriesRenderStyle.Line);
            
            // Datenreihen hinzufügen
            chart.addSeries("Schlusskurs", xData, closeData);
            chart.addSeries("Tageshoch", xData, highData);
            chart.addSeries("Tagestief", xData, lowData);
            
            // Chart anzeigen
            System.out.println("Zeige Chart...");
            new SwingWrapper<>(chart).displayChart();
            
        } catch (Exception e) {
            System.err.println("Fehler: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
