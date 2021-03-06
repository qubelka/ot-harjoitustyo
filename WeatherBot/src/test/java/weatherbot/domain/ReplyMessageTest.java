package weatherbot.domain;


import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;

public class ReplyMessageTest {

    private KeyboardBuilder keyboard;
    private ReplyMessage replyMessage;

    @Mock
    Message message;

    @Before
    public void setup() {
        keyboard = new KeyboardBuilder();
        replyMessage = new ReplyMessage(keyboard);
        message = Mockito.mock(Message.class);
        Mockito.when(message.getChatId()).thenReturn(12345678l);

    }

    @Test
    public void sendDefaultReply_CreatesDeafultReply() {
        String reply = "default";

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(keyboard.getMainMenuKeyboard())
                .setChatId(12345678l)
                .setText(reply);

        assertEquals(replyToReceived, replyMessage.sendDefaultReply(message, reply));
    }

    @Test
    public void sendUnitsReply_CreatesUnitsReply() {
        String reply = "units";

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(keyboard.getUnitsKeyboard())
                .setChatId(12345678l)
                .setText(reply);

        assertEquals(replyToReceived, replyMessage.sendUnitsReply(message, reply));
    }

}
