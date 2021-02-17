package bot.states;

import bot.Bot;
import dbService.entities.User;
import exceptions.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;
import utils.PropertyHelper;

public class PasswordState extends State {

    private static final Logger logger = LoggerFactory.getLogger(PasswordState.class);

    public PasswordState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {

        int tryCounter = user.getTryCounter();

        if(tryCounter == 0) {
            sendMessage(Messages.totalFailure);
            bot.setState(new HelloState(bot));
        } else if(!checkPwd(user, message.getText())) {
            sendMessage(Messages.pwdError + Messages.getAttempt(tryCounter));
        } else {
            user.resetTryCounter();
            sendMessage(Messages.pwdOk);
            sendMessage(Messages.reqPath);
            bot.setState(new PathState(bot));
        }
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        // nothing
    }

    private boolean checkPwd(User user, String pass) {
        try {
            return (PropertyHelper.getBotUsers().containsKey(user.getName()) &&
                    PropertyHelper.getBotUsers().get(user.getName()).equals(pass));
        } catch (PropertyException e) {
            logger.error(e.getMessage());
            return false;
        }
    }
}