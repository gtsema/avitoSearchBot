package bot.states;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class WorkState extends State {
    public WorkState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        sendMessage(Messages.stop, getStopButtons());
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        if(callback.getData().equals("stop")) {
            sendMessage(String.format(Messages.whatToDo, bot.getUser().getName()), getChangeButtons());
            bot.setState(new ChangeState(bot));
        } else {
            sendMessage(Messages.stop, getStopButtons());
        }
    }
}
