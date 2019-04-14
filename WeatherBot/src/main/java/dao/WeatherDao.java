package dao;

import domain.User;
import domain.Weather;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class WeatherDao implements Dao<Weather, Integer> {
    public static HashMap<String, Integer> cities;
    
    public WeatherDao() {
        cities = new HashMap<>();
    }

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Override
    public void create(Weather weather) throws SQLException {
        jdbcTemplate.update("INSERT INTO Weather"
                + " (id, city, temperature, humidity, main, icon)"
                + " VALUES (?, ?, ?, ?, ?, ?)",
                weather.getId(),
                weather.getCity(),
                weather.getTemperature(),
                weather.getHumidity(),
                weather.getMain(),
                weather.getIcon());
    }

    @Override
    public Weather read(Integer key) throws SQLException {
        Weather weather = jdbcTemplate.queryForObject(
                "SELECT * FROM Weather WHERE id = ?",
                new BeanPropertyRowMapper<>(Weather.class),
                key);

        return weather;
    }

    // ADD TRIGGER
    @Override
    public Weather update(Weather weather) throws SQLException {
        jdbcTemplate.update("UPDATE Weather SET temperature = ?, humidity = ?, main = ?, icon = ? WHERE id = ?",
                weather.getTemperature(),
                weather.getHumidity(),
                weather.getMain(),
                weather.getIcon(),
                weather.getId());

        return weather;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        jdbcTemplate.update("DELETE FROM Weather WHERE id = ?", key);
    }

    @Override
    public List<Weather> list() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public boolean contains(String key) {
        return cities.containsKey(key);
    }

}
