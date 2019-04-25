package weatherbot.domain;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.telegram.telegrambots.ApiContextInitializer;
import weatherbot.dao.LocationDao;
import weatherbot.dao.WeatherDao;
import weatherbot.ui.BotUi;
import weatherbot.dao.UserDao;

/**
 * The Weather-bot is a simple desktop-application for running telegram-bot @HarjoitustyoWeatherbot. 
 * The application fetches information about current weather from Openweathermap-api and returns it to the user via telegram.
 * <p>
 * The class BotApp implements Spring Framework interface CommandLineRunner.
 * 
*/

@SpringBootApplication
@ComponentScan(basePackages = {"weatherbot.ui", "weatherbot.dao"})
public class BotApp implements CommandLineRunner {

    @Autowired
    WeatherDao weatherDao;
    
    @Autowired
    UserDao userDao;
    
    @Autowired
    LocationDao locationDao;
    
    private BotUi userInterface = new BotUi();
    
    public static void main(String[] args) throws IOException {  
        SpringApplication.run(BotApp.class);
    }

   
    @Override
    public void run(String... args) throws Exception {
        ApiContextInitializer.init();
        WeatherService weatherService = new WeatherService(weatherDao, userDao, locationDao);
        userInterface.start(weatherService);
    }
}
