package weatherbot.ui;

import java.io.IOException;
import java.util.Scanner;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import weatherbot.configuration.BotConfiguration;
import weatherbot.domain.Bot;
import weatherbot.domain.BotApp;
import weatherbot.domain.KeyboardBuilder;
import weatherbot.domain.ReplyMessage;
import weatherbot.domain.WeatherService;

/**
 * The class represents user interface of the desktop bot application.
 */
public class BotUi {

    /**
     * Loads configuration files, initialises bot in telegram environment and
     * starts the botApi.
     *
     * @param weatherService weatherService handles weather requests
     * @throws IOException if application can not read configuration files
     */
    public void start(WeatherService weatherService) throws Exception {
        BotConfiguration botConfiguration = new BotConfiguration();
        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        ReplyMessage replyMessage = new ReplyMessage(keyboardBuilder);
        TelegramBotsApi botsApi = new TelegramBotsApi();
        Bot bot = new Bot(weatherService, replyMessage, botConfiguration);
        Scanner scanner = new Scanner(System.in);

        try {
            botsApi.registerBot(bot);
            bot.initDatabase();
            weatherService.userDao.load();
            System.out.println("Connection established successfully");
            System.out.println("");
            System.out.println("To try this app:\n"
                    + "1) leave this project running on NetBeans\n"
                    + "2) open telegram\n"
                    + "2) search for @HarjoitustyoWeatherbot\n"
                    + "4) type some text or press any button\n");
            System.out.println("");
            System.out.println("To stop app type \"close\" in Netbeans\n"
                    + "or CTRL+Z in bash");
            String reply = scanner.nextLine();
            if (reply.equals("close")) {
                BotApp.close = true;
                System.exit(0);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

}
