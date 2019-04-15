package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

public class BotConfiguration {

    public static String botName;
    public static String botToken;

    public static String dbUrl;
    public static String dbUser;
    public static String dbPassword;

    public static void load() throws FileNotFoundException, IOException {
        Properties botSettings = loadBotSettings();
        Properties databaseSettings = loadDatabaseSettings();

        botName = botSettings.getProperty("botName", "HarjoitustyoWeatherbot");
        botToken = botSettings.getProperty("botToken", "789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo");
        dbUrl = databaseSettings.getProperty("dbUrl", "jdbc:h2:./weather");
        dbUser = databaseSettings.getProperty("dbUser", "sa");
        dbPassword = databaseSettings.getProperty("dbPassword", "");
    }

    private static Properties loadBotSettings() {
        Properties properties = new Properties();

        try {
            InputStream in = BotConfiguration.class.getClassLoader().getResourceAsStream("bot.properties");
            properties.load(in);
            System.out.println("...");
            System.out.println("Bot configuration file has been loaded successfully");
        } catch (IOException e) {
            System.out.println("An error occured while loading bot configuration file");
        }

        return properties;
    }

    private static Properties loadDatabaseSettings() {
        Properties properties = new Properties();

        try {
            InputStream in = BotConfiguration.class.getClassLoader().getResourceAsStream("database.properties");
            properties.load(in);
            in.close();
            System.out.println("...");
            System.out.println("Database configuration file has been loaded successfully");
            System.out.println("");
        } catch (IOException e) {
            System.out.println("An error occured while loading database configuration file");
        }

        return properties;
    }

    public String getBotName() {
        return botName;
    }

    public String getBotToken() {
        return botToken;
    }

    public String getDbUrl() {
        return dbUrl;
    }

    public String getDbUser() {
        return dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }
}
