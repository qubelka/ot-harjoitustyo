package tgbots.weatherbot;

import domain.Bot;
import domain.KeyboardBuilder;
import domain.ReplyMessage;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Main {
    public static void main(String[] args) {
        ApiContextInitializer.init();

        TelegramBotsApi botsApi = new TelegramBotsApi();
        KeyboardBuilder keyboard = new KeyboardBuilder();
        ReplyMessage reply = new ReplyMessage(keyboard);
        
        try {            
            botsApi.registerBot(new Bot(reply));
            System.out.println("Connection established successfully");
            System.out.println("");
            System.out.println("To try this app:\n"
                    + "1) leave this project running on NetBeans\n"
                    + "2) open telegram\n"
                    + "2) search for @HarjoitustyoWeatherbot\n"
                    + "4) type some text or press any button\n");
            System.out.println("");
            System.out.println("To stop app just press stop (red square button in Netbeans)\n"
                    + "or CTRL+Z in bash");
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
