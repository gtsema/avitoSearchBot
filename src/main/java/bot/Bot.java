package bot;

import Exceptions.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Messages;
import utils.PropertyHelper;


public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    int choiceCounter = 3;
    private long userChatId = 0;
    private String userName = "";
    private String PATH = "";

    private enum dialogState {HELLO, PWD, PATH, NOTIFICATION, OK};
    private dialogState dialog = dialogState.HELLO;

    public Bot(String botName, String botToken) {
        this.BOT_NAME = botName;
        this.BOT_TOKEN = botToken;
    }

    @Override
    public String getBotUsername() {
        return BOT_NAME;
    }

    @Override
    public String getBotToken() {
        return BOT_TOKEN;
    }

    private synchronized void sendMsg(Long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(chatId.toString());
        answer.setText(text);
        try {
            execute(answer);
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
    }

    private void setDialogState(dialogState state) {
        dialog = state;
    }

    public synchronized void sendMsgWithKeyboard(String chatId, String s, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(s);
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
    }

    @Override
    public void onUpdateReceived(Update update) {
        String message = "";
        if(userChatId == 0) userChatId = update.getMessage().getChatId();

        if(update.hasMessage() && !(message = update.getMessage().getText()).isEmpty()) {
            switch (dialog) {
                case HELLO:
                    userName = update.getMessage().getChat().getFirstName();
                    sendMsg(userChatId, String.format(Messages.getMessage("Hello"), userName));
                    setDialogState(dialogState.PWD);
                    break;
                case PWD:
                    pwdDialogHandler(userName, message);
                    break;
                case PATH:
                    pathDialogHandler(message);
                    break;
                case NOTIFICATION:
                    break;
                case OK:
                    break;
            }
        }
    }

    private void pwdDialogHandler(String name, String pass) {
            try {
                if(PropertyHelper.getBotUsers().containsKey(name) &&
                   PropertyHelper.getBotUsers().get(name).equals(pass)) {
                    sendMsg(userChatId, Messages.getMessage("PassOk"));
                    setDialogState(dialogState.PATH);
                } else {
                    if(!checkTry()) setDialogState(dialogState.HELLO);
                }
            } catch (PropertyException e) {
                logger.error(e.getMessage());
                sendMsg(userChatId, Messages.getMessage("Error"));
                setDialogState(dialogState.HELLO);
            }
    }

    private boolean pathDialogHandler(String path) {
        return true;
    }

    private boolean checkTry() {
        if(--choiceCounter == 0) {
            choiceCounter = 3;
            sendMsg(userChatId, Messages.getMessage("TotalFailure"));
            return false;
        } else {
            sendMsg(userChatId, Messages.getMessage("Fail") +
                                     Messages.getChoise(choiceCounter));

            return true;
        }
    }
}
