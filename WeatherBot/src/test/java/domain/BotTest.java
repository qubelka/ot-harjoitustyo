package domain;

import dao.UserDao;
import dao.WeatherDao;
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
import domain.Bot;
import domain.KeyboardBuilder;
import domain.ReplyMessage;
import domain.WeatherService;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONException;
import static org.hamcrest.CoreMatchers.*;

public class BotTest {

    private Bot bot;
    private KeyboardBuilder keyboard;
    private ReplyMessage reply;
    private WeatherDao weatherDao;
    private UserDao userDao;

    @Mock
    Update update;
    Message message;
    WeatherService weatherService;

    @Before
    public void setup() throws IOException {
        keyboard = new KeyboardBuilder();
        weatherDao = new WeatherDao();
        userDao = new UserDao();
        weatherService = new WeatherService(weatherDao, userDao);
        weatherService = Mockito.mock(WeatherService.class);
        reply = new ReplyMessage(keyboard);
        bot = spy(new Bot(reply, weatherService));
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
    public void botCanReceiveHelpRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("help");
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }

    @Test
    public void botCanReceiveUnitsRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("units");
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }

    @Test
    public void botCanReceiveLocationsRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("my locations");
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }

    @Test
    public void botCanReceiveSearchRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("search by city name");
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }

    @Test
    public void botCanReceiveDefaultRequests() throws IOException, JSONException, SQLException {
        createFakeUpdateConditions();
        createFakeUpdateConditionsForWeatherSearch();
        Mockito.when(message.getText()).thenReturn("Helsinki");
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }

    public void createFakeUpdateConditions() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);

        Mockito.when(update.hasMessage()).thenReturn(true);
        Mockito.when(update.getMessage()).thenReturn(message);
        Mockito.when(update.getMessage().hasText()).thenReturn(true);
        Mockito.when(message.getChatId()).thenReturn(12345678l);
        Mockito.when(message.getMessageId()).thenReturn(1234);
    }

    public void createFakeUpdateConditionsForWeatherSearch() throws IOException, JSONException, SQLException {
        Mockito.when(weatherService.getWeather(message.getText(), message.getChatId())).thenReturn("weatherInfo");
    }

}
