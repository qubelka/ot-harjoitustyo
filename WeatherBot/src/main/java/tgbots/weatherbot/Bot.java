package tgbots.weatherbot;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class Bot extends TelegramLongPollingBot {

    @Override
    public String getBotUsername() {
        return "HarjoitustyoWeatherbot";
    }

    @Override
    public String getBotToken() {
        return "789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo";
    }

    // checkstyle error : MethodLength	--> Method length is 23 lines (max allowed is 20)
    @Override
    public void onUpdateReceived(Update update) {
        // We check if the update has a message and the message has text
        if (update.hasMessage() && update.getMessage().hasText()) {
            Message received = update.getMessage();
            SendMessage reply;
            String generalizedRequest = received.getText().replaceAll("/", "");

            switch (generalizedRequest) {
                default:
                    reply = sendDeafultReply(received, "nothing to show yet, press any button on the keyboard to test functionality");
                    break;
                case "help":
                    reply = sendHelpReply(received, "can't help you, this method not supported yet");
                    break;
                case "units":
                    reply = sendUnitsReply(received, "can't change units, this method not supported yet");
                    break;
                case "my locations":
                    reply = sendLocationsReply(received, "can't show locations, this method not supported yet");
                    break;
                case "search by city name":
                    reply = sendSearchReply(received, "can't perform search, this method not supported yet");
                    break;
            }
            try {
                execute(reply); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }
    // At the moment all the methods return the same keyboard and the same kind of answer: "method not supported yet"

    public SendMessage sendDeafultReply(Message received, String reply) {
        // create a keyboard for this reply
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        SendMessage replyToReceived = new SendMessage() // Create a SendMessage object with mandatory fields
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public SendMessage sendHelpReply(Message received, String reply) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public SendMessage sendUnitsReply(Message received, String reply) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public SendMessage sendLocationsReply(Message received, String reply) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public SendMessage sendSearchReply(Message received, String reply) {
        ReplyKeyboardMarkup replyKeyboardMarkup = getMainMenuKeyboard();

        SendMessage replyToReceived = new SendMessage()
                .setReplyMarkup(replyKeyboardMarkup)
                .setChatId(received.getChatId())
                .setText(reply);

        return replyToReceived;
    }

    public ReplyKeyboardMarkup getMainMenuKeyboard() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        List<KeyboardRow> keyboardRowList = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        keyboardFirstRow.add(new KeyboardButton("help"));
        keyboardFirstRow.add(new KeyboardButton("units"));
        keyboardSecondRow.add(new KeyboardButton("my locations"));
        keyboardSecondRow.add(new KeyboardButton("search by city name"));

        keyboardRowList.add(keyboardFirstRow);
        keyboardRowList.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboardRowList);

        return replyKeyboardMarkup;
    }

}
