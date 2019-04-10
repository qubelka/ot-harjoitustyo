package domain;


import domain.WeatherService;
import java.io.IOException;
import java.net.MalformedURLException;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class WeatherServiceTest {

    WeatherService weatherService;

    @Before
    public void setUp() {
        weatherService = new WeatherService();
    }

    @Test
    public void getWeatherWorks() throws IOException, JSONException {
        String request = "123";
        assertEquals("No weather information found for your request, please check the spelling.\nFor help: /help", weatherService.getWeather(request));
    }

}
