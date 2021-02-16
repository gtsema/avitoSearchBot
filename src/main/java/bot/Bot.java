package bot;

import dbService.entities.User;
import exceptions.DialogHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    Set<User> users;
    DialogHandler_ dialogHandler;

    public Bot(String BOT_NAME, String BOT_TOKEN) {
        super();
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        users = new HashSet<>();
        dialogHandler = new DialogHandler_();
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
        try {
            long chatId = getChatId(update);
            User user = getUser(chatId);
            executeApiMethod(dialogHandler.handleUpdate(user, update)); //<<<<<<<<<<<<< dialogHandler.setTask()??? Runnable
        } catch (IllegalArgumentException | DialogHandlerException e) {
            logger.error(e.getMessage());
        }
    }

    public void executeApiMethod(BotApiMethod<? extends Serializable> method) {
        try {
            execute(method);
        } catch (TelegramApiException e) {
            logger.error("Unable to perform api method");
        }
    }

    private User getUser(long chatId) {
        return users.stream().filter(usr -> usr.getChatId() == chatId)
                             .findFirst().orElseGet(() -> { User newUser = new User(chatId);
                                                            users.add(newUser);
                                                            return newUser; });
    }

    private long getChatId(Update update) throws IllegalArgumentException {
        if(update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if(update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            throw new IllegalArgumentException("chatId not found");
        }
    }
}
