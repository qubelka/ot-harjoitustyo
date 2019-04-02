
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import static org.mockito.ArgumentMatchers.isA;
import org.mockito.Mock;
import org.mockito.Mockito;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.MockitoJUnitRunner;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import tgbots.weatherbot.Bot;

@RunWith(MockitoJUnitRunner.class)
public class BotTest {

//    @Mock
//    Bot bot;

    
    Bot bot; 
    
    @Before
    public void init() {
        bot = new Bot();
    }
    
    @Test
    public void getUsername_ReturnsRigthUsername() {
//        Mockito.when(bot.getBotUsername()).thenReturn("HarjoitustyoWeatherbot");
        assertEquals("HarjoitustyoWeatherbot", bot.getBotUsername());
//        verify(bot).getBotUsername();
    }

    @Test
    public void getToken_ReturnsRightToken() {
//        Mockito.when(bot.getBotToken()).thenReturn("789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo");
        assertEquals("789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo", bot.getBotToken());
//        verify(bot).getBotToken();
    }
//    
//    @Test
//    public void onUpdateReceived_IsCalledCorrectly() {
//        doNothing().when(bot).onUpdateReceived(isA(Update.class));
//        Update update = new Update();
//        bot.onUpdateReceived(update);
//        bot.onUpdateReceived(update);
//        verify(bot, times(2)).onUpdateReceived(update);
//    }
    
//    @Test
//    public void getMainMenuKeyboard_ReturnsCorrectKeyboard() {
//        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
//        Mockito.when(bot.getMainMenuKeyboard()).thenReturn(replyKeyboardMarkup);
//        assertEquals(replyKeyboardMarkup, bot.getMainMenuKeyboard());
//        verify(bot).getMainMenuKeyboard();
//    }
    
}
