package weatherbot.domain;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import weatherbot.domain.KeyboardBuilder;

public class ReplyMessage {

    private KeyboardBuilder keyboardBuilder;

    public ReplyMessage(KeyboardBuilder keyboardBuilder) {
        this.keyboardBuilder = keyboardBuilder;
    }

    public SendMessage sendDefaultReply(Message received, String reply) {
        // create a keyboard for this reply
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardBuilder.getMainMenuKeyboard();
        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public SendMessage sendUnitsReply(Message received, String reply) {
        InlineKeyboardMarkup inlineKeyboardMarkup = keyboardBuilder.getUnitsKeyboard();

        SendMessage replyToReceived = new SendMessage() // Create a SendMessage object with mandatory fields
                .setReplyMarkup(inlineKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    SendMessage sendLocationsReply(Message received, String reply) {
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardBuilder.getLocationsKeyboard(reply);
        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText("Please select location from the list or add a new one: ");

        return replyToReceived;
    }

    SendMessage sendForcedReply(Message received) {
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(forceReplyKeyboard)
                .setChatId(received.getChatId())
                .setReplyToMessageId(received.getMessageId())
                .setText("Please enter city name: ");

        return replyToReceived;
    }
}
