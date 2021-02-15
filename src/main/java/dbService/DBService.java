package dbService;

import exceptions.PropertyException;
import dbService.DAO.AdvertDAO;
import dbService.entities.Advert;
import org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.PropertyHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLClientInfoException;
import java.sql.SQLException;
import java.util.*;

public class DBService {

    private static final Logger logger = LoggerFactory.getLogger(DBService.class);

    private final Connection connection;

    public DBService() {
        this.connection = getH2Connection();
    }

    private Connection getH2Connection() {
        try {
            String url = "jdbc:h2:./AdvertDb";
            String dbLogin = PropertyHelper.getDbLogin();
            String dbPass = PropertyHelper.getDbPass();

            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(url, dbLogin, dbPass);
        } catch (ClassNotFoundException e) {
            logger.error("Database driver is not found. Exit.");
            System.exit(-5);
        } catch (SQLException e) {
            logger.error("Unable to connect to database. Exit.");
            System.exit(-5);
        } catch (PropertyException e) {
            logger.error(e.getMessage());
            System.exit(-5);
        }
        return null;
    }

    public void printConnectInfo() {
        try {
            System.out.println("DB name: " + connection.getMetaData().getDatabaseProductName());
            System.out.println("DB version: " + connection.getMetaData().getDatabaseProductVersion());
            System.out.println("Driver: " + connection.getMetaData().getDriverName());
            System.out.println("Autocommit: " + connection.getAutoCommit());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            logger.error("Unable to close connection to database. Exit.");
            System.exit(-5);
        }
    }

    public void dropTable() {
        try {
            connection.setAutoCommit(false);
            AdvertDAO advertDAO = new AdvertDAO(connection);
            advertDAO.dropTable();
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException fuck) {
                e.printStackTrace();
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public List<Advert> getAdverts() {
        try {
            AdvertDAO advertDAO = new AdvertDAO(connection);
            return advertDAO.getAll();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Advert getById(int id) {
        try {
            AdvertDAO advertDAO = new AdvertDAO(connection);
            return advertDAO.getById(id);
        } catch (SQLException e) {
            logger.info("Error retrieving records from the database.");
        }
        return null;
    }

    public void createAdvertsTable() {

        AdvertDAO advertDAO = new AdvertDAO(connection);

        try {
            connection.setAutoCommit(false);
            advertDAO.createTable();
            advertDAO.clearTable();
        } catch (SQLException e) {
            try {
                e.printStackTrace();
                logger.error("Database error. Rollback and exit");
                connection.rollback();
                System.exit(e.getErrorCode());
            } catch (SQLException fuck) {
                logger.error("Database fatal error. Exit.");
                System.exit(e.getErrorCode());
            }
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                logger.error("Database fatal error. Exit.");
                System.exit(e.getErrorCode());
            }
        }
    }

    public Set<Advert> insertsAndGetAddedAdverts(Set<Advert> adverts) {
        AdvertDAO advertDAO = new AdvertDAO(connection);

        Set<Advert> successfulAdded = new HashSet<>();

        for(Advert advert : adverts) {
            try {
                advertDAO.insert(advert);
                successfulAdded.add(advert);
            } catch (JdbcSQLIntegrityConstraintViolationException e) {
                logger.info(String.format("Advert from ID %d already exists in a database", advert.getId()));
            } catch (SQLException e) {
                logger.warn("Error adding to database. Rollback.");
                try {
                    connection.rollback();
                } catch (SQLException fatal) {
                    logger.error("Database fatal error. Exit.");
                    logger.error(fatal.getMessage());
                    System.exit(fatal.getErrorCode());
                }
            } finally {
                try {
                    connection.setAutoCommit(true);
                } catch (SQLException e) {
                    logger.error("Database fatal error. Exit.");
                    System.exit(e.getErrorCode());
                }
            }
        }

        return successfulAdded;
    }
}
