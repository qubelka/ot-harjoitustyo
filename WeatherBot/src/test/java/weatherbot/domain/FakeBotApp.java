package weatherbot.domain;

import java.io.IOException;
import java.util.Scanner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import weatherbot.configuration.BotConfiguration;
import weatherbot.dao.LocationDao;
import weatherbot.dao.UserDao;
import weatherbot.dao.WeatherDao;

@SpringBootApplication
@ComponentScan(basePackages = {"weatherbot.dao"})
public class FakeBotApp implements CommandLineRunner {

    static ConfigurableApplicationContext context;

    @Autowired
    WeatherDao weatherDao;

    @Autowired
    UserDao userDao;

    @Autowired
    LocationDao locationDao;

    public static void main(String[] args) throws IOException {
        context = SpringApplication.run(FakeBotApp.class);

    }

    @Override
    public void run(String... args) throws Exception {
        ApiContextInitializer.init();
        WeatherService weatherService = new WeatherService(weatherDao, userDao, locationDao);
        BotConfiguration botConfiguration = new BotConfiguration();
        KeyboardBuilder keyboardBuilder = new KeyboardBuilder();
        ReplyMessage replyMessage = new ReplyMessage(keyboardBuilder);
        TelegramBotsApi botsApi = new TelegramBotsApi();
        Bot bot = new Bot(weatherService, replyMessage, botConfiguration);
        try {
            botsApi.registerBot(bot);
            bot.initDatabase();
            weatherService.userDao.load();
            System.out.println("Connection established successfully\n");

        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static void close() {
        context.close();
    }

}
