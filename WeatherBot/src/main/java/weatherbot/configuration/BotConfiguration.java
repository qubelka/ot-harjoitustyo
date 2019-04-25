package weatherbot.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

/**
 * This class reads bot and database configurations from properties-file and stores these configurations in static variables.
 * Used to configure the bot before starting the application in telegram. 
 * Bot configurations consist of bot name and token used to initialise the bot in telegram api. 
 * Database configurations consist of database url, user name and password used to initialise database.
*/

public class BotConfiguration {

    public String botName;
    public String botToken;

    public String dbUrl;
    public String dbUser;
    public String dbPassword;

    
    public BotConfiguration() throws IOException {
        load();
    }
    
/**
 * Reads and loads bot and database configurations from properties.
 * @throws IOException if application can not read the file
*/    
    
    public void load() throws IOException {
        Properties botSettings = loadBotSettings();
        Properties databaseSettings = loadDatabaseSettings();

        botName = botSettings.getProperty("botName", "HarjoitustyoWeatherbot");
        botToken = botSettings.getProperty("botToken", "789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo");
        dbUrl = databaseSettings.getProperty("dbUrl", "jdbc:h2:./weather");
        dbUser = databaseSettings.getProperty("dbUser", "sa");
        dbPassword = databaseSettings.getProperty("dbPassword", "");
    }

/**
 * Loads bot settings, i.e. the name and the token received after registration 
 * @return returns bot settings
 */    
    
    public Properties loadBotSettings() {
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

/**
 * Loads database settings, i.e. the url, user name and password used to initialise a h2-database for storing cache weather information 
 * @return returns database settings
 */    
    
    public Properties loadDatabaseSettings() {
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
