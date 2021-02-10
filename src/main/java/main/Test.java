package main;

import bot.Bot;
import exceptions.PropertyException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import utils.PropertyHelper;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
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
