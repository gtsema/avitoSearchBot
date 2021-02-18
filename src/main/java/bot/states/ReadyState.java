package bot.states;

import bot.Bot;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class ReadyState extends State {
    public ReadyState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        // nothing
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        if(callback.getData().equals("yes")) {
            System.out.println("start work");
            bot.setState(new WorkState(bot));
        } else {
            sendMessage(String.format(Messages.whatToDo, bot.getUser().getName()), getChangeButtons());
            bot.setState(new ChangeState(bot));
        }
    }
}
