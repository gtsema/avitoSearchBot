package test.java;

import avitoParser.Parser;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrepareResources {

    private static final Logger logger = LoggerFactory.getLogger(PrepareResources.class);

    public static final int timeout = 5000;
    public static final String correctLocalPageLink = "src/test/resources/107Adverts_3page.htm";
    public static final String correctGlobalPageLink = "https://www.avito.ru/rossiya/lichnye_veschi";
    public static final String incorrectLocalPageLink = "tra-ta-ta";
    public static final String incorrectGlobalPageLink = "https://www.google.com";

    public static List<String> getLocalFilePagesLinks() {
        return new ArrayList<>() {{
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?cd=1&localPriority=0&pmax=10000&pmin=5000&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C");
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?p=2&cd=1&localPriority=0&pmax=10000&pmin=5000&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C");
            add("http://www.avito.ru/sankt-peterburg/audio_i_video?p=3&cd=1&localPriority=0&pmax=10000&pmin=5000&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C");
        }};
    }

    public static Document getLocalFileDocument() {
        try {
            return Jsoup.parse(new File(correctLocalPageLink), "UTF-8");
        } catch (IOException e) {
            logger.error("unable to get local page");
            return Jsoup.parse("<html>empty</html>");
        }
    }

    public static Document getGlobalFileDocument() {
        try {
            return Jsoup.connect(correctGlobalPageLink).timeout(timeout).get();
        } catch (IOException e) {
            logger.error("unable to get global page");
            return Jsoup.parse("<html>empty</html>");
        }
    }
}
