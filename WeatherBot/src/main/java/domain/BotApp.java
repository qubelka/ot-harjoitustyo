package domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ui.BotUi;

@SpringBootApplication
@ComponentScan(basePackageClasses = {ui.BotUi.class})
public class BotApp implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BotApp.class);
    }

    @Autowired
    BotUi userInterface;

    @Override
    public void run(String... args) throws Exception {
        initDatabase();
        userInterface.start();

    }

    private void initDatabase() {
    }
}
