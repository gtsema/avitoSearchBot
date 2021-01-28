package dbService.DAO;

import dbService.entities.Advert;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AdvertDAO extends DAO<Advert> {

    private final Executor executor;

    public AdvertDAO(Connection connection) {
        executor = new Executor(connection);
    }

    @Override
    public void createTable() throws SQLException {
        executor.execUpdate("CREATE TABLE IF NOT EXISTS Adverts(id INT NOT NULL AUTO_INCREMENT," +
                                                               "id_adv INT NOT NULL PRIMARY KEY," +
                                                               "path VARCHAR(255) NOT NULL," +
                                                               "title VARCHAR(255) NOT NULL," +
                                                               "subway VARCHAR(255) NOT NULL," +
                                                               "distance INT NOT NULL," +
                                                               "price INT NOT NULL)");
    }

    @Override
    public void dropTable() throws SQLException {
        executor.execUpdate("drop table Adverts");
    }

    @Override
    public void insert(Advert entity) throws SQLException {
        String query = "INSERT INTO Adverts(id_adv, path, title, subway, distance, price) VALUES (%d, '%s', '%s', '%s', %d, %d)";
        executor.execUpdate(String.format(Locale.ROOT, query, entity.getId(),
                                                              entity.getPath(),
                                                              entity.getTitle(),
                                                              entity.getSubway(),
                                                              entity.getDistance(),
                                                              entity.getPrice()));
    }

    @Override
    public Advert getById(int id) throws SQLException {
        String query = "SELECT DISTINCT * FROM Adverts WHERE id = %d";
        return executor.execQuery(String.format(Locale.ROOT, query, id), result -> {
            result.next();
            return new Advert(result.getString("path"),
                              result.getInt("id_adv"),
                              result.getString("title"),
                              result.getString("subway"),
                              result.getInt("distance"),
                              result.getInt("price"));
        });
    }

    @Override
    public List<Advert> getAll() throws SQLException {
        String query = "SELECT * FROM Adverts";
        return executor.execQuery(query, result -> {
            List<Advert> adverts = new ArrayList<>();
            while (result.next()) {
                adverts.add(new Advert(result.getString("path"),
                                       result.getInt("id_adv"),
                                       result.getString("title"),
                                       result.getString("subway"),
                                       result.getInt("distance"),
                                       result.getInt("price")));
            }
            return adverts;
        });
    }

    public void clearTable() throws SQLException {
        executor.execUpdate("delete from Adverts");
    }
}
