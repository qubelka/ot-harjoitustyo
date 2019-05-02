package weatherbot.dao;

import java.sql.SQLException;
import java.util.List;
import static org.hamcrest.CoreMatchers.*;
import org.junit.After;
import weatherbot.domain.User;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = weatherbot.domain.FakeBotApp.class)
public class UserDaoTest {

    @Autowired
    UserDao userDao;

    User user;

    public UserDaoTest() throws SQLException {
        user = new User(12345678l, 1);
    }
    
    @Before
    public void setUp() throws SQLException {          
        userDao.create(user);
    }
    
     /**
     * Since Autowired-object does not exist at the moment of construction of a class, it is not possible to 
     * use it for creating a user in the constructor once before all classes. On the other hand Autowired-object can not be used
     * in static methods, so it is not possible to use it in @BeforeClass method. Since user's id is not generated automatically, adding the same user 
     * to the database before each test method would result in an error. Method tearDown() is used to prevent this from happening.
     */
    
    @After
    public void tearDown() throws SQLException {
        userDao.clear();
    }

    @Test
    public void userExistsAfterCreation() throws SQLException {
        List<User> createdUsers = userDao.list();
        assertThat(createdUsers.size(), is(1));
        assertTrue(userDao.contains(user.getId()));
    }

    @Test
    public void existingUserIsFound() throws SQLException {
        User created = userDao.read(user.getId());
        assertThat(created, is(notNullValue()));
    }

    @Test
    public void nonExistingUserIsNotFound() {
        boolean found = userDao.contains(88888888l);
        assertFalse(found);
    }

    @Test
    public void correctUserIsReturned() throws SQLException {
        User newUser = new User(99999999l, 2);
        userDao.create(newUser);

        User returnedUser = userDao.read(99999999l);
        assertEquals(99999999l, newUser.getId());
        assertEquals(2, newUser.getUnits());
    }

    @Test
    public void userUpdateWorksCorrectly() throws SQLException {
        user.setUnits(2);
        userDao.update(user);
        assertEquals(2, userDao.read(user.getId()).getUnits());
    }

    @Test
    public void userDoesNotExistAfterDeletion() throws SQLException {
        userDao.delete(user.getId());
        assertFalse(userDao.contains(user.getId()));
    }
    
    /**
     * Method load() fetches user preferences from the database before the bot is initialised.
     * This test imitates the situation when the database is empty. Original user is added to the database 
     * in the end for the tearDown() to work correctly. 
     */
    
    @Test
    public void usersAreLoadCorrecltyFromDatabase() throws SQLException {
        userDao.delete(user.getId());
        userDao.load();
        assertFalse(userDao.contains(user.getId()));
        user = userDao.create(user);
    }

}
