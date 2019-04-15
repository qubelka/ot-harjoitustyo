package dao;

import domain.User;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Component;

@Component
public class UserDao implements Dao<User, Long> {
    public final String userConfig;
    // users.txt
//    public static final String USER_CONFIG = "./src/users.txt";
    public HashMap<Long, User> users;

    public UserDao(String filePath) throws IOException {
        userConfig = filePath;
        users = new HashMap<>();
        load();
    }

    @Override
    public void create(User user) {
        users.put(user.getId(), user);
        try {
            write(user);
        } catch (IOException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public User read(Long key) {
        return users.get(key);
    }

    @Override
    public User update(User user) {
        users.put(user.getId(), user);
        try {
            updateFile(user);
        } catch (IOException ex) {
            Logger.getLogger(UserDao.class.getName()).log(Level.SEVERE, null, ex);
        }
        return user;
    }

    @Override
    public void delete(Long key) {
        users.remove(key);
    }

    public void load() throws IOException {
        File userSettings = new File(userConfig);
        if (userSettings.length() == 0) {
            return;
        }
        
        try {
            Files.lines(Paths.get(userConfig))
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

    private void write(User user) throws IOException {
        FileWriter writer = new FileWriter(userConfig);
        writer.write(user.getId() + "," + user.getUnits());
        writer.close();
    }

    private void updateFile(User user) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(userConfig);
        for (Long key : users.keySet()) {
            writer.write(users.get(key).getId() + "," + users.get(key).getUnits());
        }
        writer.close();
    }
    
    @Override
    public Set<User> list() {
        Set<User> userSet = new HashSet<>(users.values());
        return userSet;
    }


}
