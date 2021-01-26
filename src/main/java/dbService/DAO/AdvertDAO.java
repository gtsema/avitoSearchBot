package dbService.DAO;

import dbService.entitys.Advert;
import dbService.executor.Executor;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AdvertDAO extends DAO<Advert> {

    private final Executor executor;

    public AdvertDAO(Connection connection) {
        executor = new Executor(connection);
    }

    @Override
    public void createTable() throws SQLException {

    }

    @Override
    public void dropTable() throws SQLException {

    }

    @Override
    public void insert(Advert entity) throws SQLException {

    }

    @Override
    public Advert getById(int id) throws SQLException {
        return null;
    }

    @Override
    public List<Advert> getAll() throws SQLException {
        return null;
    }
}
