package states;

import bot.Bot;
import dbService.entities.User;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;

public class PasswordState extends State {

    public PasswordState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(User user, Message message) {

    }

    @Override
    public void handleCallback(User user, CallbackQuery callback) {

    }
}
