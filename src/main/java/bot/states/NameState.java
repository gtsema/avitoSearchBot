package bot.states;

import bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;

public class NameState extends State {

    private static final Logger logger = LoggerFactory.getLogger(NameState.class);

    public NameState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        bot.getUser().setName(message.getText());
        sendMessage(String.format(Messages.reqPwd, bot.getUser().getName()));
        bot.setState(new PasswordState(bot));
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        // nothing
    }
}
