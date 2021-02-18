package bot;

import dbService.entities.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import bot.states.HelloState;
import bot.states.State;

public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    private State state;
    private User user;

    public Bot(String BOT_NAME, String BOT_TOKEN) {
        super();
        this.BOT_NAME = BOT_NAME;
        this.BOT_TOKEN = BOT_TOKEN;

        this.state = new HelloState(this);
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
        long actualChatId = getChatId(update);
        if(user == null) {
            user = new User(actualChatId);
        }

        if(user.getChatId() == actualChatId) {
            state.handleUpdate(update);
        } else {
            throw new IllegalArgumentException("тара-та-тат-а-о");
        }
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public User getUser() {
        return user;
    }

    private long getChatId(Update update) {
        if(update.hasMessage()) {
            return update.getMessage().getChatId();
        } else if(update.hasCallbackQuery()) {
            return update.getCallbackQuery().getMessage().getChatId();
        } else {
            String error = "Invalid update type.";
            logger.error(error);
            throw new IllegalArgumentException(error);
        }
    }
}
