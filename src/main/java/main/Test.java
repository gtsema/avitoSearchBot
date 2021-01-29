package main;

import avitoParser.Parser;
import dbService.DBService;
import dbService.entities.Advert;

import java.io.IOException;
import java.util.List;

public class Test {

    public static void main(String[] args) throws IOException {
//        String path = "https://www.avito.ru/sankt-peterburg/sport_i_otdyh/fitnes_i_trenazhery-ASgBAgICAUTKAuAK?metro=153-173-190-192-211&pmax=40000&q=%D1%81%D0%BA%D0%B0%D0%BC%D1%8C%D1%8F+%D0%B4%D0%BB%D1%8F+%D0%BF%D1%80%D0%B5%D1%81%D1%81%D0%B0";
        String path = "D:\\tsema\\programming\\Java\\IdeaProjects\\Java\\avitoSearchBot\\example.htm";

        DBService dbService = new DBService();

        Parser parser = new Parser(path);
        List<Advert> adverts = parser.parse();

        System.out.println(adverts.size());

        dbService.createAdvertsTable();
        List<Advert> newAdverts = dbService.insertsAndGetAddedAdverts(adverts);
        dbService.closeConnection();

        System.out.println(newAdverts.size());
    }
}
