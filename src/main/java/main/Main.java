package main;

import Exceptions.PropertyException;
import bot.Bot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.PropertyHelper;

public class Main {
    public static void main(String[] args) {
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
