package com.chartanalyse;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigLoader {
    private static Properties properties = null;
    
    public static String getApiKey() {
        if (properties == null) {
            loadProperties();
        }
        return properties.getProperty("api.key");
    }
    
    private static void loadProperties() {
        properties = new Properties();
        try (FileInputStream fis = new FileInputStream("config.properties")) {
            properties.load(fis);
            System.out.println("Config erfolgreich geladen");
        } catch (IOException e) {
            System.err.println("Fehler beim Laden der config.properties: " + e.getMessage());
            System.err.println("Stelle sicher, dass config.properties existiert und einen api.key enthält");
        }
    }
}
