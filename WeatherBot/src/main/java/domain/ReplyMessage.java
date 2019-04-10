package domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

@Component
public class ReplyMessage {

    private KeyboardBuilder keyboardBuilder;

    @Autowired
    public ReplyMessage(KeyboardBuilder kb) {
        this.keyboardBuilder = kb;
    }

    public SendMessage sendDefaultReply(Message received, String reply) {
        // create a keyboard for this reply
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardBuilder.getMainMenuKeyboard();

        SendMessage replyToReceived = new SendMessage() // Create a SendMessage object with mandatory fields
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public SendMessage sendUnitsReply(Message received, String reply) {
        InlineKeyboardMarkup inlineKeyboardMarkup = keyboardBuilder.getUnitsKeyboard();

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(inlineKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }
}
