package bot;

import exceptions.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Messages;
import utils.PathChecker;
import utils.PropertyHelper;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final String BOT_NAME;
    private final String BOT_TOKEN;

    int choiceCounter = 3;
    private String userChatId = "";
    private String userName = "";
    private String PATH = "";
    private int notificationTime = 0;

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

    private synchronized void sendMsg(String chatId, String text) {
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
        if(userChatId.isEmpty()) userChatId = update.getMessage().getChatId().toString();

        if(update.hasMessage() && !(message = update.getMessage().getText()).isEmpty()) {
            switch (dialog) {
                case HELLO:
                    userName = update.getMessage().getChat().getFirstName();
                    sendMsg(userChatId, String.format(Messages.hello, userName));
                    sendMsg(userChatId, Messages.reqPwd);
                    setDialogState(dialogState.PWD);
                    break;
                case PWD:
                    pwdDialogHandler(userName, message);
                    break;
                case PATH:
                    pathDialogHandler(message);
                    break;
                case NOTIFICATION:
                    sendMsgWithKeyboard(userChatId, Messages.reqTime, getNotificationButtons());
                    break;
                case OK:
                    break;
            }
        } else if(update.hasCallbackQuery()) {
            notificationTime = Integer.parseInt(update.getCallbackQuery().getData());
            sendMsg(userChatId, Messages.notificationOk);
            sendMsg(userChatId, Messages.ok);
            setDialogState(dialogState.OK);
            startWork();
        }
    }

    private void pwdDialogHandler(String name, String pass) {
            try {
                if(PropertyHelper.getBotUsers().containsKey(name) &&
                   PropertyHelper.getBotUsers().get(name).equals(pass)) {
                    sendMsg(userChatId, Messages.pwdOk);
                    sendMsg(userChatId, Messages.reqPath);
                    setDialogState(dialogState.PATH);
                } else {
                    if(!checkTry(dialog)) setDialogState(dialogState.HELLO);
                }
            } catch (PropertyException e) {
                logger.error(e.getMessage());
                sendMsg(userChatId, Messages.prgError);
                setDialogState(dialogState.HELLO);
            }
    }

    private void pathDialogHandler(String path) {
        if(PathChecker.isValidPath(path)) {
            PATH = path;
            sendMsg(userChatId, Messages.pathOk);
            sendMsgWithKeyboard(userChatId, Messages.reqTime, getNotificationButtons());
            setDialogState(dialogState.NOTIFICATION);
        } else {
            if(!checkTry(dialog)) setDialogState(dialogState.HELLO);
        }
    }

    private boolean checkTry(dialogState dialog) {
        if(--choiceCounter == 0) {
            choiceCounter = 3;
            sendMsg(userChatId, Messages.totalFailure);
            return false;
        } else {
            String message = "";
            switch (dialog) {
                case PWD:
                    message = Messages.pwdError;
                    break;
                case PATH:
                    message = Messages.pathError;
                    break;
            }
            sendMsg(userChatId, message + Messages.getAttempt(choiceCounter));
            return true;
        }
    }

    private InlineKeyboardMarkup getNotificationButtons() {
        return InlineKeyboardMarkup.builder()
                                   .keyboardRow(new ArrayList<InlineKeyboardButton>() {{
                                                    add(InlineKeyboardButton.builder().text("1 минута").callbackData("1").build());
                                                    add(InlineKeyboardButton.builder().text("5 минут").callbackData("5").build());
                                                    add(InlineKeyboardButton.builder().text("15 минут").callbackData("15").build());}})
                                   .keyboardRow(new ArrayList<InlineKeyboardButton>() {{
                                                    add(InlineKeyboardButton.builder().text("30 минут").callbackData("30").build());
                                                    add(InlineKeyboardButton.builder().text("1 час").callbackData("60").build());
                                                    add(InlineKeyboardButton.builder().text("5 часов").callbackData("300").build());}})
                                   .build();

    }

    private void startWork() {
        System.out.println("start");
    }
}
