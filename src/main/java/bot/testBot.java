package bot;

import dbService.entities.User;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.Set;

public class testBot extends TelegramLongPollingBot {
    private final String BOT_NAME;
    private final String BOT_TOKEN;

    Set<User> users;
    DialogHandler dialogHandler;

    public testBot(String BOT_NAME, String BOT_TOKEN) {
        super();
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    @Override
    public void onUpdateReceived(Update update) {
        System.out.println(update.getMessage().getText());

        int messageid = update.getMessage().getMessageId();
        delete(this, getChatId(update), messageid);

        int id = send(this, getChatId(update), "ololo");

        delete(this, getChatId(update), id);
    }

    private long getChatId(Update update) {
        if(update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if(update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            throw new IllegalArgumentException("chatId not found");
        }
    }

    private void delete(AbsSender absSender, long chatId, int messageId) {
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(String.valueOf(chatId));
        delete.setMessageId(messageId);
        try {
            absSender.execute(delete);
        } catch (TelegramApiException e) {
            System.out.println("Error interacting with Telegram api.");
        }
    }

    private int send(AbsSender absSender, long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(String.valueOf(chatId));
        answer.setText(text);


        try {
            Message msg = absSender.execute(answer);
            return msg.getMessageId();
        } catch (TelegramApiException e) {
            System.out.println("Error interacting with Telegram api.");
        }
        return 0;
    }
}
