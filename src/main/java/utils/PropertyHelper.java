package utils;

import exceptions.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PropertyHelper {

    private static final Logger logger = LoggerFactory.getLogger(PropertyHelper.class);

    private static final String PROPERTIES_FILE = "config.properties";

    private static final String DB_DEFAULT_LOGIN = "admin";
    private static final String DB_DEFAULT_PASS = "admin";

    public static Map<String, String> getBotUsers() throws PropertyException {
        try {
            String users = getProperty("botUsers");
            if(users.isEmpty()) throw new IllegalArgumentException();
            else {
                return Stream.of(users.split(",")).collect(Collectors.toMap(x -> x.split(":")[0],
                        x -> x.split(":")[1]));
            }
        } catch (IOException e) {
            throw new PropertyException("Can not read the properties file.");
        } catch (IllegalArgumentException e) {
            throw new PropertyException("In file of properties is empty botUsers.");
        }
    }

    public static String getDbLogin() throws PropertyException {
        try {
            String dbLogin = getProperty("dbLogin");
            if(dbLogin.isEmpty()) throw new IllegalArgumentException();
            else return dbLogin;
        } catch (IOException e) {
            throw new PropertyException("Can not read the properties file.");
        } catch (IllegalArgumentException e) {
            logger.warn("In file of properties is empty database login. Will be used default login.");
            return DB_DEFAULT_LOGIN;
        }
    }

    public static String getDbPass() throws PropertyException {
        try {
            String dbPass = getProperty("dbPass");
            if(dbPass.isEmpty()) throw new IllegalArgumentException();
            else return dbPass;
        } catch (IOException e) {
            throw new PropertyException("Can not read the properties file.");
        } catch (IllegalArgumentException e) {
            logger.warn("In file of properties is empty database password. Will be used default password.");
            return DB_DEFAULT_PASS;
        }
    }

    public static String getBotName() throws PropertyException {
        try {
            String botUsername = getProperty("botUsername");
            if(botUsername.isEmpty()) throw new IllegalArgumentException();
            else return botUsername;
        } catch (IOException e) {
            throw new PropertyException("Can not read the properties file.");
        } catch (IllegalArgumentException e) {
            throw new PropertyException("In file of properties is empty bot username.");
        }
    }

    public static String getBotToken() throws PropertyException {
        try {
            String botToken = getProperty("botToken");
            if(botToken.isEmpty()) throw new IllegalArgumentException();
            else return botToken;
        } catch (IOException e) {
            throw new PropertyException("Can not read the properties file.");
        } catch (IllegalArgumentException e) {
            throw new PropertyException("In file of properties is empty bot token.");
        }
    }

    private static String getProperty(String attribute) throws IOException {
        Properties properties = new Properties();
        properties.load(new FileInputStream(PROPERTIES_FILE));
        return properties.getProperty(attribute);
    }
}
