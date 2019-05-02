package weatherbot.domain;

import weatherbot.dao.UserDao;
import weatherbot.dao.WeatherDao;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONException;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import weatherbot.configuration.BotConfiguration;
import weatherbot.dao.LocationDao;

public class BotTest {

    private Bot bot;
    private KeyboardBuilder keyboard;
    private ReplyMessage reply;
    private WeatherDao weatherDao;
    private UserDao userDao;
    private LocationDao locationDao;
    private BotConfiguration botConfig;

    @Mock
    Update update;
    Message message;
    CallbackQuery callbackQuery;
    WeatherService weatherService;

    @Before
    public void setup() throws IOException, SQLException {
        keyboard = new KeyboardBuilder();
        weatherDao = new WeatherDao();
        userDao = new UserDao();
        botConfig = new BotConfiguration();
        locationDao = new LocationDao();
        weatherService = new WeatherService(weatherDao, userDao, locationDao);
        weatherService = Mockito.mock(WeatherService.class);
        reply = new ReplyMessage(keyboard);
        bot = spy(new Bot(weatherService, reply, botConfig));
    }

    @Test
    public void getUsername_ReturnsRigthUsername() {
        Mockito.when(bot.getBotUsername()).thenReturn("HarjoitustyoWeatherbot");
        assertEquals("HarjoitustyoWeatherbot", bot.getBotUsername());
        verify(bot).getBotUsername();
    }

    @Test
    public void getToken_ReturnsRightToken() {
        Mockito.when(bot.getBotToken()).thenReturn("789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo");
        assertEquals("789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo", bot.getBotToken());
        verify(bot).getBotToken();
    }

    @Test
    public void onUpdateReceived_IsCalledCorrectly() {
        doNothing().when(bot).onUpdateReceived(isA(Update.class));
        Update update = new Update();
        bot.onUpdateReceived(update);
        bot.onUpdateReceived(update);
        verify(bot, times(2)).onUpdateReceived(update);
    }

    @Test
    public void botCanReceiveStartRequest() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("start");
        executeUpdate();
    }

    @Test
    public void botCanReceiveBackRequest() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("back");
        executeUpdate();
    }

    @Test
    public void botCanReceiveAddNewLocationRequest() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("add new location");
        executeUpdate();
    }

    @Test
    public void botCanReceiveHelpRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("help");
        executeUpdate();
    }

    @Test
    public void botCanReceiveUnitsRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("units");
        executeUpdate();
    }

    @Test
    public void botCanReceiveLocationsRequests() throws SQLException {
        createFakeUpdateConditions();
        createFakeUpdateConditionsForLocationsRequest();
        Mockito.when(message.getText()).thenReturn("my locations");
        executeUpdate();
    }

    @Test
    public void botCanReceiveSearchRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("search by city name");
        executeUpdate();
    }

    @Test
    public void botCanReceiveDefaultRequests() throws IOException, JSONException, SQLException {
        createFakeUpdateConditions();
        createFakeUpdateConditionsForWeatherSearch();
        Mockito.when(message.getText()).thenReturn("Helsinki");
        executeUpdate();
    }

    @Test
    public void botCanCatchUserReply() throws SQLException {
        createFakeUpdateConditions();
        createFakeUpdateConditionsForLocationsRequest();
        Mockito.when(message.isReply()).thenReturn(true);
        Mockito.when(message.getText()).thenReturn("Athens");
        executeUpdate();
    }

    @Test
    public void botCanCatchCallbackQueryForCelciusUnits() {
        createFakeUpdateConditionsForCallBackQuery();
        Mockito.when(update.getCallbackQuery().getData()).thenReturn("update_celcius");
        executeUpdate();
    }

    @Test
    public void botCanCatchCallbackQueryForFahrenheitUnits() {
        createFakeUpdateConditionsForCallBackQuery();
        Mockito.when(update.getCallbackQuery().getData()).thenReturn("update_fahrenheit");
        executeUpdate();
    }

    public void createFakeUpdateConditions() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);

        Mockito.when(update.hasMessage()).thenReturn(true);
        Mockito.when(update.getMessage()).thenReturn(message);
        Mockito.when(update.getMessage().hasText()).thenReturn(true);
        Mockito.when(message.getChatId()).thenReturn(12345678l);
    }

    public void createFakeUpdateConditionsForWeatherSearch() throws IOException, JSONException, SQLException {
        Mockito.when(weatherService.getWeather(message.getText(), message.getChatId())).thenReturn("weatherInfo");
    }

    public void createFakeUpdateConditionsForLocationsRequest() throws SQLException {
        Mockito.when(weatherService.getLocations(message.getChatId())).thenReturn("Oslo");
        Mockito.when(weatherService.addLocation(message.getText(), message.getChatId())).thenReturn("location added successfully");
    }

    public void createFakeUpdateConditionsForCallBackQuery() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);
        callbackQuery = Mockito.mock(CallbackQuery.class);
        Mockito.when(update.getMessage()).thenReturn(message);
        Mockito.when(update.getMessage().hasText()).thenReturn(true);
        Mockito.when(update.hasCallbackQuery()).thenReturn(true);
        Mockito.when(update.getCallbackQuery()).thenReturn(callbackQuery);
        Mockito.when(update.getCallbackQuery().getMessage()).thenReturn(message);
        Mockito.when(update.getCallbackQuery().getMessage().getChatId()).thenReturn(12345678l);
    }

    public void executeUpdate() {
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }

}
