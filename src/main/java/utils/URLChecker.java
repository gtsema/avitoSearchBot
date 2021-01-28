package utils;

import java.net.URL;

public class URLChecker {
    public static boolean isValidURL(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
