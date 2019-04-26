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
    
    /**
     * Starts the Spring boot application
     * @param args default 
     * @throws IOException if the components required for starting the application can not be found
     */
    public static void main(String[] args) throws IOException {  
        SpringApplication.run(BotApp.class);
    }

    /**
     * Initialises the bot context and launches the method used to start the bot application
     * @see weatherbot.ui.BotUi#start(weatherbot.domain.WeatherService) 
     * @param args the class launching the spring boot application
     * @throws Exception if the class launching the spring boot application can not be found or the bot environment can not be initialised
     */
    @Override
    public void run(String... args) throws Exception {
        ApiContextInitializer.init();
        WeatherService weatherService = new WeatherService(weatherDao, userDao, locationDao);
        userInterface.start(weatherService);
    }
}
