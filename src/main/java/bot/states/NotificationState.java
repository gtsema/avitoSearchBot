package bot.states;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class NotificationState extends State {
    public NotificationState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        // nothing
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        user.setNotificationTime(Integer.parseInt(callback.getData()));
        sendMessage(Messages.ok, getYesNoButtons());
        bot.setState(new ReadyState(bot));
    }
}
