package weatherbot.domain;

import java.util.Objects;

public class Location {

    private Integer id;
    private String location;
    private long userId;

    public Location() {

    }

    public Location(String location) {
        this(0, location, 0l);
    }

    public Location(String location, long userId) {
        this(0, location, userId);
    }

    public Location(Integer id, String location, long userId) {
        this.id = id;
        this.location = location;
        this.userId = userId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + Objects.hashCode(this.location);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Location other = (Location) obj;
        if (!Objects.equals(this.location, other.location)) {
            return false;
        }
        return true;
    }
    
    @Override
    public String toString() {
        return this.location;
    }

}
