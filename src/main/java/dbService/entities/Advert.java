package dbService.entities;

import java.util.Objects;

public class Advert implements Entity {

    private String path;
    private int id;
    private String title;
    private String subway;
    private int distance;
    private int price;

    public Advert(String path, int id, String title, String subway, int distance, int price) {
        this.path = path;
        this.id = id;
        this.title = title;
        this.subway = subway;
        this.distance = distance;
        this.price = price;
    }

    public String getPath() {
        return path;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getSubway() {
        return subway;
    }

    public int getDistance() {
        return distance;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Advert advert = (Advert) o;
        return id == advert.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
