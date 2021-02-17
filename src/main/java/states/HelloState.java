package states;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class HelloState extends State {

    public HelloState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        user.setName(message.getFrom().getFirstName());
        sendMessage(user.getChatId(), String.format(Messages.hello, user.getName()), getYesNoButtons());
        bot.setState(new PasswordState(bot));
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        // nothing
    }
}
