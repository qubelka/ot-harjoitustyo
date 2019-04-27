package weatherbot.domain;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ForceReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;

import weatherbot.domain.KeyboardBuilder;

/**
 * This class is used to create reply messages that are sent as answers to user requests.
 */
public class ReplyMessage {

    private KeyboardBuilder keyboardBuilder;

    /**
     * Creates Keyboardbuilder object when the class is constructed
     * @param keyboardBuilder keyboardBuilder creates keyboards
     */
    public ReplyMessage(KeyboardBuilder keyboardBuilder) {
        this.keyboardBuilder = keyboardBuilder;
    }

    /**
     * Creates and returns default reply (i.e. reply on a weather request)
     * @param received the message received from the user
     * @param reply weather information retrieved from openweathermap api or error message if information was not found
     * @return returns SendMessage object that is returned to user as reply
     */
    public SendMessage sendDefaultReply(Message received, String reply) {
        // create a keyboard for this reply
        ReplyKeyboardMarkup replyKeyboardMarkup = keyboardBuilder.getMainMenuKeyboard();
        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    /**
     * Creates and returns reply for unit settings request (i.e. when the user want to set unit preferences)
     * @param received the message received from the user
     * @param reply a message confirming that units were set correctly or an error message if the user was not found
     * @return returns SendMessage object that is returned to user as reply
     */
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
                .setText("Please select a location from the list or add a new one: ");

        return replyToReceived;
    }

    SendMessage sendForcedReply(Message received) {
        ForceReplyKeyboard forceReplyKeyboard = new ForceReplyKeyboard();
        forceReplyKeyboard.setSelective(true);
        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(forceReplyKeyboard)
                .setChatId(received.getChatId())
                .setReplyToMessageId(received.getMessageId())
                .setText("Please enter a city name: ");

        return replyToReceived;
    }
}
