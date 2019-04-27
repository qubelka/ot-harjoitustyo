package weatherbot.domain;

import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.LogManager;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import weatherbot.configuration.BotConfiguration;

/**
 * This class extends TelegramLongPollingBot and represents a Bot-entity.
 */
public class Bot extends TelegramLongPollingBot {

    private static final Logger LOGGER;

    static {
        InputStream stream = Bot.class.getClassLoader().
                getResourceAsStream("logging.properties");
        try {
            LogManager.getLogManager().readConfiguration(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        LOGGER = Logger.getLogger(Bot.class.getName());
    }

    private BotConfiguration botConfiguration;
    private WeatherService weatherService;
    private ReplyMessage replyMessage;

    /**
     * Creates a new bot entity with injected parameters
     *
     * @param weatherService weatherService handles weather requests
     * @param replyMessage replyMessage creates reply messages for user replies
     * @param botConfiguration botConfiguration loads bot and database settings
     * from properties-files
     */
    public Bot(WeatherService weatherService, ReplyMessage replyMessage, BotConfiguration botConfiguration) {
        this.botConfiguration = botConfiguration;
        this.weatherService = weatherService;
        this.replyMessage = replyMessage;
    }

    /**
     * Returns the name of the registered bot. This method is used to initialise
     * the bot in telegram.
     *
     * @see weatherbot.ui.BotUi#start(WeatherService weatherService)
     * @return returns the name of the bot
     */
    @Override
    public String getBotUsername() {
        return botConfiguration.botName;
    }

    /**
     * Returns the token of the registered bot. This method is used to
     * initialise the bot in telegram
     *
     * @see weatherbot.ui.BotUi#start(WeatherService weatherService)
     * @return returns the token of the bot
     */
    @Override
    public String getBotToken() {
        return botConfiguration.botToken;
    }

    /**
     * Handles updates received via telegram-api. First checks if the update has
     * a message and the message has text. If there is a message and it contains
     * text, executes the command received in message and returns the result.
     *
     * @param update An update object tells what kind of interaction is going on
     * between the user and the bot
     */
    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message received = update.getMessage();
            Long userID = received.getChatId();
            SendMessage reply = replyMessage.sendDefaultReply(received, "No weather information found for your request, please check the spelling.\nFor help: /help");
            String generalizedRequest = received.getText().replaceAll("/", "").replaceAll("_", " ");
            switch (generalizedRequest) {
                case "start":
                    reply = replyMessage.sendDefaultReply(received, getWelcomeText());
                    break;
                case "back":
                    reply = replyMessage.sendDefaultReply(received, getHelpText());
                    break;
                case "add new location":
                    reply = replyMessage.sendForcedReply(received);
                    break;
                case "help":
                    reply = replyMessage.sendDefaultReply(received, getHelpText());
                    break;
                case "units":
                    reply = replyMessage.sendUnitsReply(received, "Please choose your preferred units:");
                    break;
                case "my locations":
                    try {
                        reply = replyMessage.sendLocationsReply(received, weatherService.getLocations(userID));
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "null");
                    }
                    break;
                case "search by city name":
                    reply = replyMessage.sendDefaultReply(received, "Please enter a city name: ");
                    break;
                default:
                    if (received.isReply()) {
                        try {
                            reply = replyMessage.sendDefaultReply(received, weatherService.addLocation(generalizedRequest, userID));
                        } catch (SQLException ex) {
                            LOGGER.log(Level.SEVERE, "null");
                        }
                        break;
                    }
                    try {
                        reply = replyMessage.sendDefaultReply(received, weatherService.getWeather(generalizedRequest, userID));
                    } catch (JSONException ex) {
                        LOGGER.log(Level.SEVERE, "null");
                    } catch (IOException ex) {
                        LOGGER.log(Level.SEVERE, "null");
                    } catch (SQLException ex) {
                        LOGGER.log(Level.SEVERE, "null");
                    }
                    break;
            }
            try {
                // id used for testing 12345678; condition created for testing
                if (reply.getChatId().equals("12345678")) {
                    return;
                }
                execute(reply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        } else if (update.hasCallbackQuery()) {
            Message callback = update.getCallbackQuery().getMessage();
            String callData = update.getCallbackQuery().getData();
            SendMessage reply = replyMessage.sendDefaultReply(callback, "Oops, something went wrong. Press any button to continue.");
            if (callData.equals("update_celcius")) {
                reply = replyMessage.sendDefaultReply(callback, "Current weather units: °C");
                try {
                    weatherService.setUnits(callback.getChatId(), 1);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "null");
                }
            } else if (callData.contains("update_fahrenheit")) {
                reply = replyMessage.sendDefaultReply(callback, "Current weather units: °F");
                try {
                    weatherService.setUnits(callback.getChatId(), 2);
                } catch (SQLException ex) {
                    LOGGER.log(Level.SEVERE, "null");
                }
            }

            try {
                // id used for testing 12345678; condition created for testing
                if (reply.getChatId().equals("12345678")) {
                    return;
                }
                execute(reply);
            } catch (TelegramApiException e) {
                e.printStackTrace();
                 LOGGER.log(Level.SEVERE, "Exception in executing callbackquery");
            }
        }
    }

    /**
     * Returns text with help instructions. This method is used to generate an
     * answer for the help request.
     *
     * @see
     * weatherbot.domain.Bot#onUpdateReceived(org.telegram.telegrambots.meta.api.objects.Update)
     * @return returns a String object with help instructions
     */
    private String getHelpText() {
        return "To check the weather: "
                + "type a city name or choose one from\n /my_locations. To set the units:\n /units. To add new location:\n /my_locations";
    }

    private String getWelcomeText() {
        String welcome = "The Weather-bot is a simple application for searching weather. "
                + "The information about the current weather is fetched from Openweathermap-api. "
                + "To check the weather: type a city name or choose one from\n /my_locations. To set units:\n /units. To add new location:\n /my_locations";

        return welcome;
    }

    /**
     * Initialises the database used to store user and weather information.
     */
    public void initDatabase() throws SQLException {
        Connection conn = null;

        try {
            conn = DriverManager.getConnection(botConfiguration.dbUrl, botConfiguration.dbUser, botConfiguration.dbPassword);
            conn.prepareStatement("DROP TABLE Weather IF EXISTS;").executeUpdate();
            conn.prepareStatement(getWeatherTable()).executeUpdate();
            conn.prepareStatement("DROP TABLE User IF EXISTS;").executeUpdate();
            conn.prepareStatement(getUserTable()).executeUpdate();
            conn.prepareStatement("DROP TABLE Locations IF EXISTS;").executeUpdate();
            conn.prepareStatement(getLocationsTable()).executeUpdate();
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "null");
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    /**
     * Returns a sql request used to create a Weather table
     *
     * @return returns a string with sql request
     */
    public String getWeatherTable() {
        String weather = "CREATE TABLE Weather (\n"
                + "    id INTEGER AUTO_INCREMENT PRIMARY KEY,\n"
                + "    city VARCHAR(300),\n"
                + "    temperature VARCHAR(100),\n"
                + "    humidity VARCHAR(100),\n"
                + "    main VARCHAR(200),\n"
                + "    icon VARCHAR(300),"
                + "    time TIMESTAMP\n"
                + ");";

        return weather;
    }

    /**
     * Returns a sql request used to create a User table
     *
     * @return returns a string with sql request
     */
    public String getUserTable() {
        String user = "CREATE TABLE User (\n"
                + "    id INTEGER PRIMARY KEY,\n"
                + "    units INTEGER\n"
                + ");";

        return user;
    }

    /**
     * Returns a sql request used to create a Locations table
     *
     * @return returns a string with sql request
     */
    public String getLocationsTable() {
        String locations = "CREATE TABLE Locations (\n"
                + "    id INTEGER AUTO_INCREMENT PRIMARY KEY,\n"
                + "    location VARCHAR(300),\n"
                + "    user_id INTEGER,\n"
                + "    FOREIGN KEY (user_id) REFERENCES User(id)\n"
                + ");";

        return locations;
    }
}
