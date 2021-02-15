package avitoParser;

import dbService.entities.Advert;
import exceptions.ParserException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.URLChecker;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Parser {

    private static final Logger logger = LoggerFactory.getLogger(Parser.class);

    private String globalPath = "http://www.avito.ru";

    public Set<Advert> parse(String path) throws ParserException {
        Set<Advert> adverts = new HashSet<>();

        try {
            if(URLChecker.isValidURL(path)) {
                Set<String> pages = getPages(path);
                for(String page : pages) {
                    adverts.addAll(parsePage(page));
                }
                return adverts;
            } else {
                return parsePage(path);
            }
        } catch (Exception e) { // <---------------------------------
            logger.error(e.getMessage());
            throw new ParserException(e.getCause());
        }
    }

    private Set<String> getPages(String path) throws IOException {
        Set<String> result = new HashSet<>();

        Document doc = getDoc(path);
        Element pagination = doc.getElementsByAttributeValue("class", "pagination-pages clearfix").last();

        if(pagination == null) {
            logger.info("Only one page found.");
            result.add(path);
        } else {
            Elements pages = pagination.getElementsByAttribute("href");
            for(Element page : pages) {
                if(page.text().matches("[0-9]")) {
                    result.add("http://www.avito.ru" + page.attr("href"));
                }
            }
        }
        return result;
    }

    private Set<Advert> parsePage(String path) throws IOException {
        Document doc = getDoc(path);
        Elements advertsElements = doc.getElementsByAttributeValueStarting("class", "iva-item-root");

        if(advertsElements.size() == 0) throw new IOException("Adverts not found!");

        return new HashSet<>() {{
            for(Element advertElement : advertsElements) {
                try {
                   Advert newAdvert = new Advert(getAdvertPath(advertElement),
                                                 getAdvertId(advertElement),
                                                 getAdvertTitle(advertElement),
                                                 getAdvertSubway(advertElement),
                                                 getAdvertDistance(advertElement),
                                                 getAdvertPrice(advertElement));

                    if(!add(newAdvert)) logger.info(String.format("Announcement with ID: %d already exist", newAdvert.getId()));
                } catch (IllegalArgumentException e) {
                    logger.warn("Unable to get ID or PATH adverts. " + e.getMessage());
                }
            }
        }};
    }

    private Document getDoc(String path) throws IOException {
        if(URLChecker.isValidURL(path)) {
            return Jsoup.connect(path).timeout(5000).get();
        } else {
            return Jsoup.parse(new File(path), "UTF-8");
        }
    }




    private String getAdvertPath(Element advert) throws IllegalArgumentException {
        String path;
        if((path = advert.getElementsByAttributeValueStarting("class", "link-link")
                         .attr("href"))
                         .isEmpty()) {
            throw new IllegalArgumentException("Entry does not contain \"href\" field and will be skipped.");
        } else {
            return globalPath + path;
        }
    }

    private int getAdvertId(Element advert) throws IllegalArgumentException {
        try {
            return Integer.parseInt(advert.attr("data-item-id"));
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Entry does not contain \"id\" field and will be skipped.");
        }
    }

    private String getAdvertTitle(Element advert) throws IllegalArgumentException {
        String title;
        if((title = advert.getElementsByAttributeValueStarting("class", "title-root").text()).isEmpty()) {
            throw new IllegalArgumentException("Entry does not contain \"title\" field and will be skipped.");
        } else {
            return title;
        }
    }

    private String getAdvertSubway(Element advert) {
        String subway;
        try {
            if((subway = advert.getElementsByAttributeValueStarting("class", "geo-georeferences")
                    .select("span")
                    .get(1)
                    .text())
                    .isEmpty()) {
                                    logger.warn("Entry does not contain \"subway\" field or it is empty.");
                                    return "-";
                                } else {
                                    return subway;
                                }
        } catch (Exception e) {
            logger.warn("Entry does not contain \"subway\" field or it is empty.");
            return "-";
        }
    }

    private int getAdvertDistance(Element advert) {
        double distance;
        try {
            distance = Double.parseDouble(advert.getElementsByAttributeValueStarting("class", "geo-georeferences")
                                                .select("span")
                                                .get(2)
                                                .text()
                                                .split(" ")[1].replace(",", "."));

            String unit = advert.getElementsByAttributeValueStarting("class", "geo-georeferences")
                                .select("span")
                                .get(2)
                                .text()
                                .split(" ")[2];

            if(unit.equals("м")) {
                return (int) distance;
            } else if(unit.equals("км")) {
                return (int) (distance * 1e3);
            } else throw new NumberFormatException();

        } catch (NumberFormatException e) {
            logger.warn("Unable to convert \"distance\" field.");
            return -1;
        } catch (Exception e) {
            logger.warn("Unable to found \"distance\" field.");
            return -1;
        }
    }

    private int getAdvertPrice(Element advert) throws IllegalArgumentException {
        try {
            return Integer.parseInt(advert.getElementsByAttributeValueStarting("class", "price-price")
                                          .select("meta")
                                          .get(1)
                                          .attr("content"));
        } catch (NumberFormatException e) {
            logger.warn("Entry does not contain \"price\" field or it is empty.");
            return 0;
        }
    }
}
