package domain;

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.hamcrest.CoreMatchers.*;

public class WeatherTest {

    Weather weather;

    @Before
    public void setUp() {
        weather = new Weather();
    }

    @Test
    public void weatherObjectExistAfterCreating() {
        assertThat(weather, is(notNullValue()));
    }

    @Test
    public void idIsSetCorrectly() {
        weather.setId(1);
        assertThat(weather.getId(), is(1));
    }

    @Test
    public void cityIsSetCorrectly() {
        weather.setCity("Helsinki");
        assertThat(weather.getCity(), is("Helsinki"));
    }

    @Test
    public void temperatureIsSetCorrectly() {
        weather.setTemperature("1.0");
        assertThat(weather.getTemperature(), is("1.0"));
    }

    @Test
    public void humidityIsSetCorrectly() {
        weather.setHumidity("60");
        assertThat(weather.getHumidity(), is("60"));
    }

    @Test
    public void mainIsSetCorrectly() {
        weather.setMain("Clear");
        assertThat(weather.getMain(), is("Clear"));
    }

    @Test
    public void iconIsSetCorrectly() {
        weather.setIcon("08");
        assertThat(weather.getIcon(), is("08"));
    }

    @Test
    public void toStringReturnsInformationInRightFormat() {
        createWeatherConditions();

        String correctAnswer = "City: Kouvola\n"
                + "Temperature: -1.0°C\n"
                + "Humidity: 40%\n"
                + "Main: Snow\n"
                + "http://openweathermap.org/img/w/56.png";

        assertThat(weather.toString(), is(correctAnswer));
    }

    @Test
    public void toStringFarenheitReturnsInformationInRightFormat() {
        createWeatherConditions();

        String correctAnswer = "City: Kouvola\n"
                + "Temperature: 30.2°F\n"
                + "Humidity: 40%\n"
                + "Main: Snow\n"
                + "http://openweathermap.org/img/w/56.png";

        assertThat(weather.toStringFarenheit(), is(correctAnswer));
    }

    private void createWeatherConditions() {
        weather.setCity("Kouvola");
        weather.setHumidity("40");
        weather.setMain("Snow");
        weather.setTemperature("-1.0");
        weather.setIcon("56");
    }

}
