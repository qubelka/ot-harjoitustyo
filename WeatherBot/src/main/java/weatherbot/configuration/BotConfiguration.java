package weatherbot.configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

/**
 * This class reads bot and database configurations from properties-file and
 * stores these configurations in static variables. Used to configure the bot
 * before starting the application in telegram. Bot configurations consist of
 * bot name and token used to initialise the bot in telegram api. Database
 * configurations consist of database url, user name and password used to
 * initialise database.
 */
public class BotConfiguration {

    private static final Logger LOGGER;

    static {
        InputStream stream = BotConfiguration.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER = Logger.getLogger(BotConfiguration.class.getName());
    }
    /**
     * The name of the bot registered in telegram api. The name of the current
     * bot is "HarjoitustyoWeatherbot".
     */
    public String botName;

    /**
     * The token of the bot registered in telegram api. The token of the current
     * bot is "789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo".
     */
    public String botToken;

    /**
     * The url with database name used to establish connection to h2-database.
     * The url of the current bot is "jdbc:h2:./weather". Bot stores information
     * in a database called "weather".
     */
    public String dbUrl;

    /**
     * The username used to establish the connection to h2-database ("sa" by
     * default).
     */
    public String dbUser;

    /**
     * The password used to establish the connection to h2-database (empty by
     * default).
     */
    public String dbPassword;

    /**
     * Loads all of the bots' configurations when the class is constructed.
     *
     * @throws IOException if application can not read the file
     */
    public BotConfiguration() throws IOException {
        load();
    }

    /**
     * Reads and loads bot and database configurations from properties.
     *
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
     * Loads bot settings, i.e. the name and the token received after
     * registration
     *
     * @return returns bot settings
     */
    public Properties loadBotSettings() {
        Properties properties = new Properties();

        try {
            InputStream in = BotConfiguration.class.getClassLoader().getResourceAsStream("bot.properties");
            properties.load(in);
            LOGGER.info("Bot configuration file has been loaded successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occured while loading bot configuration file");
        }

        return properties;
    }

    /**
     * Loads database settings, i.e. the url, user name and password used to
     * initialise a h2-database for storing cache weather information
     *
     * @return returns database settings
     */
    public Properties loadDatabaseSettings() {
        Properties properties = new Properties();

        try {
            InputStream in = BotConfiguration.class.getClassLoader().getResourceAsStream("database.properties");
            properties.load(in);
            in.close();
            LOGGER.info("Database configuration file has been loaded successfully");
            System.out.println("");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "An error occured while loading database configuration file");
        }

        return properties;
    }

    /**
     * @return returns bot name
     */
    public String getBotName() {
        return botName;
    }

    /**
     * @return returns bot token
     */
    public String getBotToken() {
        return botToken;
    }

    /**
     * @return returns database url
     */
    public String getDbUrl() {
        return dbUrl;
    }

    /**
     * @return returns username for creating database connection
     */
    public String getDbUser() {
        return dbUser;
    }

    /**
     * @return returns password for creating database connection
     */
    public String getDbPassword() {
        return dbPassword;
    }
}
