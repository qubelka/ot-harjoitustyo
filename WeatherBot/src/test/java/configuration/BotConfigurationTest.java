package configuration;

import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class BotConfigurationTest {
    BotConfiguration configuration;
    
    @Before
    public void setUp() throws IOException {
        configuration = new BotConfiguration();
        configuration.load();
    }
    
    @Test
    public void botNameCanBeLoadedFromConfig() {
        assertThat(configuration.getBotName(), is("HarjoitustyoWeatherbot"));
    }
    
    @Test
    public void botTokenCanBeLoadedFromConfig() {
        assertThat(configuration.getBotToken(), is("789067379:AAH7xM9S9Th-cKqdYbFowXIEBzb8vF3z3wo"));
    }
    
    @Test
    public void dbUrlCanBeLoadedFromConfig() {
        assertThat(configuration.getDbUrl(), is("jdbc:h2:./weather"));
    }
    
    @Test
    public void dbUserCanBeLoadedFromConfig() {
        assertThat(configuration.getDbUser(), is("sa"));
    }
    
    @Test
    public void dbPasswordCanBeLoadedFromConfig() {
        assertThat(configuration.getDbPassword(), is(""));
    }
}
