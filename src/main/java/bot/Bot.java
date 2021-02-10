package bot;

import dbService.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashSet;
import java.util.Set;

public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    Set<User> users;
    DialogHandler dialogHandler;

    public Bot(String BOT_NAME, String BOT_TOKEN) {
        super();
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        users = new HashSet<>();
        dialogHandler = new DialogHandler(this);
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
        long chatId = getChatId(update);
        User user = users.stream()
                         .filter(usr -> usr.getChatId() == chatId)
                         .findFirst().orElseGet(() -> { User newUser = new User(chatId);
                                                        users.add(newUser);
                                                        return newUser; });

        dialogHandler.handleUpdate(user, update);
    }

    private long getChatId(Update update) {
        if(update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if(update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            logger.error("chatId not found");
            throw new IllegalArgumentException("chatId not found");
        }
    }
}
