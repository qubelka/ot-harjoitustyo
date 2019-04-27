package weatherbot.dao;

import java.sql.PreparedStatement;
import weatherbot.domain.Weather;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import weatherbot.domain.User;

/**
 * This class is used to store and retrieve information about user locations
 * using database. UserDao implements Dao interface and represents User entity.
 *
 * @see weatherbot.dao.Dao
 */
@Component
public class WeatherDao implements Dao<Weather, Integer> {

    @Autowired
    JdbcTemplate jdbcTemplate;

    /**
     * Keeps track of the weather conditions added to the database. Prevents
     * from making unnecessary database searches.
     */
    public HashMap<String, Integer> cities;

    /**
     * Creates an empty HashMap-structure when the class is constructed.
     */
    public WeatherDao() {
        cities = new HashMap<>();
    }

    @Override
    public Weather create(Weather weather) throws SQLException {
        String sql = "INSERT INTO Weather (city, temperature, humidity, main, icon, time) "
                + "VALUES (?, ?, ?, ?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, weather.getCity());
            stmt.setString(2, weather.getTemperature());
            stmt.setString(3, weather.getHumidity());
            stmt.setString(4, weather.getMain());
            stmt.setString(5, weather.getIcon());
            stmt.setTimestamp(6, weather.getTime());
            return stmt;
        }, keyHolder);

        Integer id = (int) keyHolder.getKey();
        weather.setId(id);
        cities.put(weather.getCity().toLowerCase(), id);

        return weather;
    }

    @Override
    public Weather read(Integer key) throws SQLException {
        Weather weather = jdbcTemplate.queryForObject(
                "SELECT * FROM Weather WHERE id = ?",
                new BeanPropertyRowMapper<>(Weather.class),
                key);

        return weather;
    }

    @Override
    public Weather update(Weather weather) throws SQLException {
        jdbcTemplate.update("UPDATE Weather SET temperature = ?, humidity = ?, main = ?, icon = ?, time = ? WHERE id = ?",
                weather.getTemperature(),
                weather.getHumidity(),
                weather.getMain(),
                weather.getIcon(),
                weather.getTime(),
                weather.getId());

        return weather;
    }

    @Override
    public void delete(Integer key) throws SQLException {
        Weather weather = read(key);
        jdbcTemplate.update("DELETE FROM Weather WHERE id = ?", key);
        cities.remove(weather.getCity().toLowerCase());
    }

    @Override
    public List<Weather> list() throws SQLException {
        return jdbcTemplate.query("SELECT * FROM Weather", new BeanPropertyRowMapper<>(Weather.class));
    }

    /**
     * Checks if a weather from a certain city has already been added to the
     * database.
     *
     * @param key city name
     * @return returns true if weather has been added to the database, otherwise
     * returns false
     */
    public boolean contains(String key) {
        return cities.containsKey(key);
    }

    @Override
    public void clear() throws SQLException {
        jdbcTemplate.update("DELETE FROM Weather");
        cities.clear();
    }

}
