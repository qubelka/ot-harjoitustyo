package domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {
    ReplyMessage replyMessage;
    
    public Bot(ReplyMessage rm) {
        replyMessage = rm;
    }
    
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
            String generalizedRequest = received.getText().replaceAll("/", "").replaceAll("_", " ");

            switch (generalizedRequest) {
                default:
                    reply = replyMessage.sendDefaultReply(received, "nothing to show yet, press any button on the keyboard to test functionality");
                    break;
                case "help":
                    reply = replyMessage.sendDefaultReply(received, getHelpText());
                    break;
                case "units":
                    reply = replyMessage.sendUnitsReply(received, "can't change units, this method not supported yet");
                    break;
                case "my locations":
                    reply = replyMessage.sendDefaultReply(received, "can't show locations, this method not supported yet");
                    break;
                case "search by city name":
                    reply = replyMessage.sendDefaultReply(received, "can't perform search, this method not supported yet");
                    break;
            }
            try {
                execute(reply); // Call method to send the message
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String getHelpText() {
        return "To check weather: type city name or choose one from\n /my_locations. To set units:\n /units. To add new location:\n /my_locations";
    }
    
   

}
