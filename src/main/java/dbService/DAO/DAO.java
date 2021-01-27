package dbService.DAO;

import dbService.entities.Entity;

import java.sql.SQLException;
import java.util.List;

public abstract class DAO<T extends Entity> {

    public abstract void createTable() throws SQLException;

    public abstract void dropTable() throws SQLException;

    public abstract void clearTable() throws SQLException;

    public abstract void insert(T entity) throws SQLException;

    public abstract T getById(int id) throws SQLException;

    public abstract List<T> getAll() throws SQLException;
}
