package main;

import bot.Bot;
import bot.testBot;
import exceptions.PropertyException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.PropertyHelper;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
//        String path = "https://www.avito.ru/sankt-peterburg/sport_i_otdyh/fitnes_i_trenazhery-ASgBAgICAUTKAuAK?metro=153-173-190-192-211&pmax=40000&q=%D1%81%D0%BA%D0%B0%D0%BC%D1%8C%D1%8F+%D0%B4%D0%BB%D1%8F+%D0%BF%D1%80%D0%B5%D1%81%D1%81%D0%B0";
//        String path = "D:\\tsema\\programming\\Java\\IdeaProjects\\Java\\avitoSearchBot\\example.htm";
//
//        DBService dbService = new DBService();
//
//        Parser parser = new Parser(path);
//        List<Advert> adverts = parser.parse();
//
//        System.out.println(adverts.size());
//
//        dbService.createAdvertsTable();
//        List<Advert> newAdverts = dbService.insertsAndGetAddedAdverts(adverts);
//        dbService.closeConnection();
//
//        System.out.println(newAdverts.size());

        try {
            String botName = PropertyHelper.getBotName();
            String botToken = PropertyHelper.getBotToken();

            TelegramBotsApi api = new TelegramBotsApi(DefaultBotSession.class);
            api.registerBot(new Bot(botName, botToken));
        } catch (TelegramApiException | PropertyException e) {
            e.printStackTrace();
        }
    }
}
