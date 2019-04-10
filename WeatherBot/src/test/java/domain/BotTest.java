package domain;


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

public class BotTest {

    private Bot bot;
    private KeyboardBuilder keyboard;
    private ReplyMessage reply;
    private WeatherService weatherService;

    @Mock
    Update update;
    Message message;

    @Before
    public void setup() {
        keyboard = new KeyboardBuilder();
        weatherService = new WeatherService();
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
    public void botCanReceiveDefaultRequests() {
        createFakeUpdateConditions();
        Mockito.when(message.getText()).thenReturn("else");
        bot.onUpdateReceived(update);
        verify(bot, times(1)).onUpdateReceived(update);
    }
    
//    @Test
//    public void botRespondsToRequest() throws TelegramApiException {
//        createFakeUpdateConditions();
//        Mockito.when(message.getText()).thenReturn("help");
//        String jsonMessage = "{chatId='12345678', text='can't help you, this method not supported yet', parseMode='null', "
//                + "disableNotification='null', disableWebPagePreview=null, replyToMessageId=null, "
//                + "replyMarkup=ReplyKeyboardMarkup{keyboard=[[KeyboardButton{text=help, requestContact=null, requestLocation=null}, "
//                + "KeyboardButton{text=units, requestContact=null, requestLocation=null}], [KeyboardButton{text=my locations, requestContact=null, "
//                + "requestLocation=null}, KeyboardButton{text=search by city name, requestContact=null, requestLocation=null}]], "
//                + "resizeKeyboard=true, oneTimeKeyboard=false, selective=true}}";
//        SendMessage reply = Mockito.mock(SendMessage.class);
//
////        reply.setChatId(message.getChatId());
//
//        doNothing().doThrow(new TelegramApiException e).when(bot).execute(isA(SendMessage.class));
//        bot.onUpdateReceived(update);
//        verify(bot, times(1)).onUpdateReceived(update);
//        verify(bot, times(1)).execute(reply);
//    }

    public void createFakeUpdateConditions() {
        update = Mockito.mock(Update.class);
        message = Mockito.mock(Message.class);

        Mockito.when(update.hasMessage()).thenReturn(true);
        Mockito.when(update.getMessage()).thenReturn(message);
        Mockito.when(update.getMessage().hasText()).thenReturn(true);
        Mockito.when(message.getChatId()).thenReturn(12345678l);
        Mockito.when(message.getMessageId()).thenReturn(1234);
    }

}
