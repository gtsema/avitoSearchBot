package bot.states;

import bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class HelloState extends State {

    private static final Logger logger = LoggerFactory.getLogger(HelloState.class);

    public HelloState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        user.setName(message.getFrom().getFirstName());
        sendMessage(String.format(Messages.hello, user.getName()), getYesNoButtons());
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        if(callback.getData().equals("yes")) {
            sendMessage(String.format(Messages.reqPwd, user.getName()));
            bot.setState(new PasswordState(bot));
        } else {
            sendMessage(Messages.reqName);
            bot.setState(new NameState(bot));
        }
    }
}
