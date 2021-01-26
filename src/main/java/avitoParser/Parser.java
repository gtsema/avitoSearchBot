package avitoParser;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

public class Parser {
    private String path;

    public Parser(String path) {
        this.path = path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public List<Advert> parse() throws IOException {
        List<Advert> adverts = new LinkedList<>();
        List<String> pages = getPages(path);
        for(String page : pages) {
            adverts.add(parsePage(page));
        }
        return adverts;
    }

    private List<String> getPages(String path) throws IOException {
        Document doc = Jsoup.connect(path).timeout(5000).get();
        Element pagination = doc.getElementsByAttributeValue("class", "pagination-pages clearfix").last();
        Elements pages = pagination.getElementsByAttribute("href");
        List<String> result = new LinkedList<>();
        for(Element page : pages) {
            if(page.text().matches("[0-9]")) {
                result.add("http://www.avito.ru" + page.attr("href"));
            }
        }
        return result;
    }

    private Advert parsePage(String path) throws IOException {
        Document doc = Jsoup.connect(path).timeout(5000).get();

    }
}
