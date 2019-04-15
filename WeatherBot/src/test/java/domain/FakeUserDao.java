package domain;

import dao.Dao;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FakeUserDao implements Dao<User, Long> {
    public HashMap<Long, User> users = new HashMap<>();
    
    public FakeUserDao() {
        users.put(12345678l, new User(12345678l, 1));
    }
    
    @Override
    public void create(User user) {
         users.put(user.getId(), user);
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
    public Set<User> list() {
        Set<User> userSet = new HashSet<>(users.values());
        return userSet;
    }

    public boolean contains(Long key) {
        return users.containsKey(key);
    }
    
}
