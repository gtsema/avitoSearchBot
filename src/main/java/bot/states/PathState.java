package bot.states;

import bot.Bot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import utils.Messages;
import utils.PathChecker;

public class PathState extends State {

    private static final Logger logger = LoggerFactory.getLogger(PathState.class);

    public PathState(Bot bot) {
        super(bot);
    }

    @Override
    public void handleMessage(Message message) {
        int tryCounter = bot.getUser().getTryCounter();

        if(!PathChecker.isValidPath(message.getText()) && tryCounter != 0) {
            sendMessage(Messages.pathError + Messages.getAttempt(tryCounter));
        } else if(!PathChecker.isValidPath(message.getText()) && tryCounter == 0) {
            bot.getUser().resetTryCounter();
            sendMessage(Messages.totalFailure);
            bot.setState(new HelloState(bot));
        } else {
            bot.getUser().resetTryCounter();
            sendMessage(Messages.pathOk);
            sendMessage(Messages.reqTime, getNotificationButtons());
            bot.setState(new NotificationState(bot));
        }
    }

    @Override
    public void handleCallback(CallbackQuery callback) {
        // nothing
    }
}
