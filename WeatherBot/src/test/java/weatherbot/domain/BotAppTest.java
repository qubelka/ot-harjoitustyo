package weatherbot.domain;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import weatherbot.dao.UserDao;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * This test class is constructed to check that application context starts properly.
*/

@RunWith(SpringRunner.class)
@SpringBootTest
public class BotAppTest {
    
    @Autowired
    UserDao userDao;
    
    @Test
    public void contextLoads() throws Exception {  
        assertThat(userDao).isNotNull();
    }
}
