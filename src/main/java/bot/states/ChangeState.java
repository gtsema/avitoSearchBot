package bot.states;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class ChangeState extends State {
    public ChangeState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        // nothing
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        if(callback.getData().equals("path&time")) {
            sendMessage(Messages.reqPath);
            bot.setState(new PathState(bot));
        } else {
            sendMessage(String.format(Messages.bye, bot.getUser().getName()));
            bot.setState(new HelloState(bot));
        }
    }
}
