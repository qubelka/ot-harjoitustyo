package configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BotConfiguration {

    private static final String BOT_CONFIG = "./config/bot/bot.properties";
    private static final String DB_CONFIG = "./config/database/database.properties";

    public static String botName;
    public static String botToken;

    public static String dbUrl;
    public static String dbUser;
    public static String dbPassword;

    
    public static void load() throws FileNotFoundException, IOException {
        Properties botSettings = new Properties();
        try (InputStream is = new FileInputStream(new File(BOT_CONFIG))) {
            botSettings.load(is);
            is.close();
            System.out.println("...");
            System.out.println("Bot configuration file has been loaded successfully");
        } catch (Exception e) {
            System.out.println("An error occured while loading bot configuration file");
        }

        Properties databaseSettings = new Properties();

        try (InputStream is = new FileInputStream(new File(DB_CONFIG))) {
            databaseSettings.load(is);
            is.close();
            System.out.println("...");
            System.out.println("Database configuration file has been loaded successfully");
            System.out.println("");
        } catch (Exception e) {
            System.out.println("An error occured while loading database configuration file");
        }

       
        botName = botSettings.getProperty("botName", "HarjoitustyoWeatherbot");
        botToken = botSettings.getProperty("botToken", "789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo");

        dbUrl = databaseSettings.getProperty("dbUrl", "jdbc:h2:./weather");
        dbUser = databaseSettings.getProperty("dbUser", "sa");
        dbPassword = databaseSettings.getProperty("dbPassword", "");

    }
}
