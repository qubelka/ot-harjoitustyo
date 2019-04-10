package domain;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Component
public class Bot extends TelegramLongPollingBot {

    private ReplyMessage replyMessage;
    private WeatherService weatherService;

    @Autowired
    public Bot(ReplyMessage rm, WeatherService ws) {
        this.replyMessage = rm;
        this.weatherService = ws;
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
            Long userID = received.getChatId();
            SendMessage reply = replyMessage.sendDefaultReply(received, "No weather information found for your request, please check the spelling.\nFor help: /help");
            String generalizedRequest = received.getText().replaceAll("/", "").replaceAll("_", " ");
            switch (generalizedRequest) {
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
                    reply = replyMessage.sendDefaultReply(received, "Please enter city name: ");
                    break;
                default:
                    try {
                        reply = replyMessage.sendDefaultReply(received, weatherService.getWeather(generalizedRequest, userID));
                    } catch (JSONException ex) {
                        Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("JSONException");
                    } catch (IOException ex) {
                        Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("IOException");
                    } catch (SQLException ex) {
                        Logger.getLogger(Bot.class.getName()).log(Level.SEVERE, null, ex);
                        System.out.println("SQLException");
                    }

                    break;
            }
            try {
                execute(reply); 
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
        }
    }

    private String getHelpText() {
        return "To check weather: type city name or choose one from\n /my_locations. To set units:\n /units. To add new location:\n /my_locations";
    }

}
