package bot;

import avitoParser.Parser;
import dbService.DBService;
import dbService.entities.Advert;
import dbService.entities.User;
import exceptions.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.bots.AbsSender;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import utils.Messages;
import utils.PathChecker;
import utils.PropertyHelper;

import java.util.*;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    private final AbsSender absSender;
    private int choiceCounter;
    private final Set<Integer> botMessagesId;

    public DialogHandler(AbsSender absSender) {
        this.absSender = absSender;

        botMessagesId = new TreeSet<>();
        choiceCounter = 3;
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    public void handleUpdate(User user, Update update) {
        if(update.hasMessage() && !update.getMessage().getText().isEmpty()) {
            handleMessage(user, update.getMessage());
        } else if(update.hasCallbackQuery()) {
            handleCallback(user, update.getCallbackQuery());
        } else {
            logger.warn("sssadfqwdfefsd");
            send(user.getChatId(), Messages.prgError);
        }
    }

    private void handleMessage(User user, Message message) {

        delete(message.getChatId(), message.getMessageId());
        botMessagesId.forEach(e -> delete(user.getChatId(), e));
        botMessagesId.clear();

        switch (user.getDialogState()) {
            case HELLO:
                user.setName(message.getFrom().getFirstName());
                send(user.getChatId(), String.format(Messages.hello, user.getName()), getYesNoButtons());
                break;
            case NAME:
                user.setName(message.getText());
                send(user.getChatId(), String.format(Messages.reqPwd, user.getName()));
                user.setDialogState(User.dialogStates.PWD);
                break;
            case PWD:
                pwdDialogHandler(user, message.getText());
                break;
            case PATH:
                pathDialogHandler(user, message.getText());
                break;
            case NOTIFICATION:
                send(user.getChatId(), Messages.reqTime, getNotificationButtons());
                break;
            case READY:
                send(user.getChatId(), Messages.ok, getYesNoButtons());
                break;
            case WORK:
                send(user.getChatId(), Messages.stop, getStopButtons());
                break;
            case CHANGE:
                send(user.getChatId(), String.format(Messages.whatToDo, user.getName()), getChangeButtons());
                break;
        }
    }

    private void handleCallback(User user, CallbackQuery callbackQuery) {

        botMessagesId.forEach(e -> delete(user.getChatId(), e));
        botMessagesId.clear();

        switch (user.getDialogState()) {
            case HELLO:
                if(callbackQuery.getData().equals("yes")) {
                    send(user.getChatId(), String.format(Messages.reqPwd, user.getName()));
                    user.setDialogState(User.dialogStates.PWD);
                } else {
                    send(user.getChatId(), Messages.reqName);
                    user.setDialogState(User.dialogStates.NAME);
                }
                break;
            case NOTIFICATION:
                user.setNotificationTime(Integer.parseInt(callbackQuery.getData()));
                send(user.getChatId(), Messages.notificationOk);
                send(user.getChatId(), Messages.ok, getYesNoButtons());
                user.setDialogState(User.dialogStates.READY);
                break;
            case READY:
                if(callbackQuery.getData().equals("yes")) {
                    startWork(user);
                    user.setDialogState(User.dialogStates.WORK);
                } else {
                    send(user.getChatId(), String.format(Messages.whatToDo, user.getName()), getChangeButtons());
                    user.setDialogState(User.dialogStates.CHANGE);
                }
                break;
            case WORK:
                if(callbackQuery.getData().equals("stop")) {
                    send(user.getChatId(), String.format(Messages.whatToDo, user.getName()), getChangeButtons());
                    user.setDialogState(User.dialogStates.CHANGE);
                } else {
                    send(user.getChatId(), Messages.stop, getStopButtons());
                }
                break;
            case CHANGE:
                if(callbackQuery.getData().equals("path&time")) {
                    send(user.getChatId(), Messages.reqPath);
                    user.setDialogState(User.dialogStates.PATH);
                } else {
                    send(user.getChatId(), String.format(Messages.bye, user.getName()));
                    user.setDialogState(User.dialogStates.HELLO);
                }
                break;
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void send(long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(String.valueOf(chatId));
        answer.setText(text);

        try {
            int id = absSender.execute(answer).getMessageId();
            botMessagesId.add(id);
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
    }

    private void send(long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboardMarkup);

        try {
            int id = absSender.execute(sendMessage).getMessageId();
            botMessagesId.add(id);
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
    }

    private void delete(long chatId, int messageId) {
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(String.valueOf(chatId));
        delete.setMessageId(messageId);
        try {
            absSender.execute(delete);
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void pwdDialogHandler(User user, String pass) {
        try {
            if(PropertyHelper.getBotUsers().containsKey(user.getName()) &&
               PropertyHelper.getBotUsers().get(user.getName()).equals(pass)) {
                send(user.getChatId(), Messages.pwdOk);
                send(user.getChatId(), Messages.reqPath);
                user.setDialogState(User.dialogStates.PATH);
            } else {
                Map.Entry<Boolean, String> tryAnswer = checkTry(user.getDialogState());
                if(!tryAnswer.getKey()) {
                    send(user.getChatId(), tryAnswer.getValue());
                    user.setDialogState(User.dialogStates.HELLO);
                } else {
                    send(user.getChatId(), tryAnswer.getValue());
                }
            }
        } catch (PropertyException e) {
            logger.error(e.getMessage());
            send(user.getChatId(), Messages.prgError);
            user.setDialogState(User.dialogStates.HELLO);
        }
    }

    private void pathDialogHandler(User user, String path) {
        if(PathChecker.isValidPath(path)) {
            user.setPath(path);
            send(user.getChatId(), Messages.pathOk);
            send(user.getChatId(), Messages.reqTime, getNotificationButtons());
            user.setDialogState(User.dialogStates.NOTIFICATION);
        } else {
            Map.Entry<Boolean, String> tryAnswer = checkTry(user.getDialogState());
            if(!tryAnswer.getKey()) {
                send(user.getChatId(), tryAnswer.getValue());
                user.setDialogState(User.dialogStates.HELLO);
            } else {
                send(user.getChatId(), tryAnswer.getValue());
            }
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private Map.Entry<Boolean, String> checkTry(User.dialogStates dialogState) {
        if(--choiceCounter == 0) {
            choiceCounter = 3;
            return Map.entry(false, Messages.totalFailure);
        } else {
            switch (dialogState) {
                case PWD:
                    return Map.entry(true, Messages.pwdError + Messages.getAttempt(choiceCounter));
                case PATH:
                    return Map.entry(true, Messages.pathError + Messages.getAttempt(choiceCounter));
                default: return Map.entry(false, Messages.prgError);
            }
        }
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private InlineKeyboardMarkup getYesNoButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Да").callbackData("yes").build());
                    add(InlineKeyboardButton.builder().text("Нет").callbackData("no").build());
                }})
                .build();
    }

    private InlineKeyboardMarkup getNotificationButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("1 минута").callbackData("1").build());
                    add(InlineKeyboardButton.builder().text("5 минут").callbackData("5").build());
                    add(InlineKeyboardButton.builder().text("15 минут").callbackData("15").build());
                }})
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("30 минут").callbackData("30").build());
                    add(InlineKeyboardButton.builder().text("1 час").callbackData("60").build());
                    add(InlineKeyboardButton.builder().text("5 часов").callbackData("300").build());
                }})
                .build();

    }

    private InlineKeyboardMarkup getStopButtons() {
        return InlineKeyboardMarkup.builder()
                                   .keyboardRow(new ArrayList<>() {{
                                       add(InlineKeyboardButton.builder().text("Прекратить").callbackData("stop").build());
                                   }})
                                   .build();
    }

    private InlineKeyboardMarkup getChangeButtons() {
        return InlineKeyboardMarkup.builder()
                                   .keyboardRow(new ArrayList<>() {{
                                       add(InlineKeyboardButton.builder().text("Изменить строку поиска и время").callbackData("path&time").build());
                                   }})
                                   .keyboardRow(new ArrayList<>() {{
                                       add(InlineKeyboardButton.builder().text("Попрощаться").callbackData("bye").build()); }})
                                   .build();
    }

    //- - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    private void startWork(User user) {
        DBService dbService = new DBService();
        dbService.createAdvertsTable();

        Parser parser = new Parser(user.getPath());
        List<Advert> adverts = parser.parse();

        int newAdvertsSize = dbService.insertsAndGetAddedAdverts(adverts).size();
        send(user.getChatId(), String.format(Messages.addAdverts, newAdvertsSize));

        dbService.closeConnection();

        ScheduledThreadPoolExecutor exec = new ScheduledThreadPoolExecutor(1);

        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                DBService dbService = new DBService();
                Parser parser = new Parser(user.getPath());
                List<Advert> adverts = parser.parse();
                List<Advert> newAdverts = dbService.insertsAndGetAddedAdverts(adverts);
                dbService.closeConnection();

                for(Advert advert : newAdverts) {
                    send(user.getChatId(), advert.getPath());
                }
            }
        }, 0, user.getNotificationTime(), TimeUnit.MINUTES);
    }
}
