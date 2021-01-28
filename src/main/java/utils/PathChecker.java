package utils;

import bot.Bot;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

public class PathChecker {

    private static final Logger logger = LoggerFactory.getLogger(PathChecker.class);

    public static boolean isValidPath(String path) {

        Document doc;

        try {
            if(URLChecker.isValidURL(path)) {
                doc = Jsoup.connect(path).timeout(5000).get();
            } else {
                doc = Jsoup.parse(new File(path), "UTF-8");
            }
        } catch (IOException e) {
            logger.error("Impossible to check the correct path");
            return false;
        }

        Elements advertsElements = doc.getElementsByAttributeValueStarting("class", "iva-item-root");

        return advertsElements.size() > 0;
    }
}
