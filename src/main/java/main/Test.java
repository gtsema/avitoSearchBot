package main;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import utils.URLChecker;

import java.io.File;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        String path = "C:/Windows/explorer.exe";
        Document doc = getDoc(path);
        System.out.println(doc.body().val());
    }

    public static Document getDoc(String path) throws IOException {
        if(URLChecker.isValidURL(path)) {
            return Jsoup.connect(path).timeout(5000).get();
        } else {
            return Jsoup.parse(new File(path), "UTF-8");
        }
    }
}
