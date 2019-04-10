package domain;

import domain.WeatherService;
import java.io.IOException;
import java.sql.SQLException;
import org.json.JSONException;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.telegram.telegrambots.meta.api.objects.Message;
import static org.hamcrest.CoreMatchers.*;

public class WeatherServiceTest {

    WeatherService weatherService;

    @Before
    public void setUp() {
        weatherService = new WeatherService();
    }

    @Test
    public void createdWeatherObjectNotNull() {
        assertThat(weatherService, is(notNullValue()));
    }

    @Test
    public void getWeatherWorks() throws IOException, JSONException, SQLException {
        String request = "H3lsinki";
        assertEquals("No weather information found for your request, please check the spelling.\nFor help: /help", weatherService.getWeather(request, 12345678l));
    }

}
