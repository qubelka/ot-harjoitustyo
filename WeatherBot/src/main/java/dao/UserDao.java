package dao;

import domain.User;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class UserDao implements Dao<User, Long> {
    public static final String USER_CONFIG = "./src/users.txt";
    public static HashMap<Long, User> users;
    
    public UserDao() throws IOException {
        users = new HashMap<>();
        load();
    }
    
    @Override
    public void create(User user) {
        users.put(user.getId(), user);
        // doesn't write to file yet
    }

    
    @Override
    public User read(Long key) {
        return users.get(key);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public void delete(Long key) {
        users.remove(key);
    }

    @Override
    public List<User> list() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public void load() throws IOException {
        try {
            Files.lines(Paths.get(USER_CONFIG))
                    .map(row -> row.split(","))
                    .map(info -> new User(Long.valueOf(info[0]), Integer.valueOf(info[1])))
                    .forEach(user -> users.put(user.getId(), user));
        } catch (IOException e) {
            System.out.println("An error occured while reading file");
        }
    }
    
    public boolean contains(Long key) {
        return users.containsKey(key);
    }

}
