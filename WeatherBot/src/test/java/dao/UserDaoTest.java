package dao;

import domain.FakeUserDao;
import domain.User;
import java.io.File;
import java.io.FileWriter;
import java.util.Set;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

public class UserDaoTest {
    @Rule
    public TemporaryFolder testFolder = new TemporaryFolder();
    
    File userFile;  
    UserDao userDao;
    User user;
    
    @Before
    public void setUp() throws Exception {
        userFile = testFolder.newFile("users.txt");  
        user = new User(12345678l, 1);
        userDao = new UserDao("users.txt");
        userDao.create(user);
    }
   
    
    @Test
    public void usersAreReadCorrectlyFromFile() {
        Set<User> users = userDao.list();
        assertEquals(1, users.size());
        User user = users.stream().findFirst().get();
        assertEquals(12345678l, user.getId());
        assertEquals(1, user.getUnits());
    }
    
    @Test
    public void existingUserIsFound() {
        User user = userDao.read(12345678l);
        assertEquals(12345678l, user.getId());
        assertEquals(1, user.getUnits());
    }
    
    @Test
    public void nonExistingUserIsFound() {
        boolean found = userDao.contains(88888888l);
        assertEquals(false, found);
    }
  
    @Test
    public void savedUserIsFound() throws Exception {
        User newUser = new User(99999999l, 1);
        userDao.create(newUser);
        
        User user = userDao.read(99999999l);
        assertEquals(99999999l, user.getId());
        assertEquals(1, user.getUnits());
    }
    
    @Test
    public void userUpdateWorksCorrectly() {
        user.setUnits(2);
        userDao.update(user);
        assertEquals(2, userDao.read(user.getId()).getUnits());
    }
    
    @Test
    public void userDoesNotExistAfterDeletion() {
        userDao.delete(user.getId());
        assertEquals(false, userDao.contains(user.getId()));
    }
    
    @After
    public void tearDown() {
        userFile.delete();
    }
    
}
