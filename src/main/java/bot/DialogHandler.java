package bot;

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

import java.util.ArrayList;
import java.util.Map;

public class DialogHandler {
    private static final Logger logger = LoggerFactory.getLogger(Bot.class);

    int choiceCounter = 3;

    public void handleUpdate(AbsSender absSender, User user, Update update) {
        if(update.hasMessage() && !update.getMessage().getText().isEmpty()) {
            handleMessage(absSender, user, update.getMessage());
        } else if(update.hasCallbackQuery()) {
            handleCallback(absSender, user, update.getCallbackQuery());
        } else {
            logger.warn("sssadfqwdfefsd");
            send(absSender, user.getChatId(), Messages.prgError);
        }
    }

    private void handleMessage(AbsSender absSender, User user, Message message) {

        delete(absSender, message.getChatId(), message.getMessageId());

        switch (user.getDialogState()) {
            case HELLO:
                user.setName(message.getFrom().getFirstName());
                send(absSender, user.getChatId(), String.format(Messages.hello, user.getName()), getYesNoButtons());
                break;
            case NAME:
                user.setName(message.getText());
                send(absSender, user.getChatId(), String.format(Messages.reqPwd, user.getName()));
                user.setDialogState(User.dialogStates.PWD);
                break;
            case PWD:
                pwdDialogHandler(absSender, user, message.getText());
                break;
            case PATH:
                pathDialogHandler(absSender, user, message.getText());
                break;
            case NOTIFICATION:
                send(absSender, user.getChatId(), Messages.reqTime, getNotificationButtons());
                break;
            case READY:
                send(absSender, user.getChatId(), Messages.ok, getYesNoButtons());
                break;
            case WORK:
                send(absSender, user.getChatId(), Messages.stop, getStopButtons());
                break;
            case CHANGE:
                send(absSender, user.getChatId(), String.format(Messages.whatToDo, user.getName()), getChangeButtons());
                break;
        }
    }

    private void handleCallback(AbsSender absSender, User user, CallbackQuery callbackQuery) {

        delete(absSender, callbackQuery.getMessage().getChatId(), callbackQuery.getMessage().getMessageId());

        switch (user.getDialogState()) {
            case HELLO:
                if(callbackQuery.getData().equals("yes")) {
                    send(absSender, user.getChatId(), String.format(Messages.reqPwd, user.getName()));
                    user.setDialogState(User.dialogStates.PWD);
                } else {
                    send(absSender, user.getChatId(), Messages.reqName);
                    user.setDialogState(User.dialogStates.NAME);
                }
                break;
            case NOTIFICATION:
                user.setNotificationTime(Integer.parseInt(callbackQuery.getData()));
                send(absSender, user.getChatId(), Messages.notificationOk);
                send(absSender, user.getChatId(), Messages.ok, getYesNoButtons());
                user.setDialogState(User.dialogStates.READY);
                break;
            case READY:
                if(callbackQuery.getData().equals("yes")) {
                    send(absSender, user.getChatId(), "Работаем");
                    user.setDialogState(User.dialogStates.WORK);
                } else {
                    send(absSender, user.getChatId(), String.format(Messages.whatToDo, user.getName()), getChangeButtons());
                    user.setDialogState(User.dialogStates.CHANGE);
                }
                break;
            case WORK:
                if(callbackQuery.getData().equals("stop")) {
                    send(absSender, user.getChatId(), String.format(Messages.whatToDo, user.getName()), getChangeButtons());
                    user.setDialogState(User.dialogStates.CHANGE);
                } else {
                    send(absSender, user.getChatId(), Messages.stop, getStopButtons());
                }
                break;
            case CHANGE:
                if(callbackQuery.getData().equals("path&time")) {
                    send(absSender, user.getChatId(), Messages.reqPath);
                    user.setDialogState(User.dialogStates.PATH);
                } else {
                    send(absSender, user.getChatId(), String.format(Messages.bye, user.getName()));
                    user.setDialogState(User.dialogStates.HELLO);
                }
                break;
        }
    }

    private void delete(AbsSender absSender, long chatId, int messageId) {
        DeleteMessage delete = new DeleteMessage();
        delete.setChatId(String.valueOf(chatId));
        delete.setMessageId(messageId);
        try {
            absSender.execute(delete);
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
    }

    private int send(AbsSender absSender, long chatId, String text) {
        SendMessage answer = new SendMessage();
        answer.setChatId(String.valueOf(chatId));
        answer.setText(text);

        try {
            return absSender.execute(answer).getMessageId();
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
        }
        return 0;
    }

    private int send(AbsSender absSender, long chatId, String text, InlineKeyboardMarkup keyboardMarkup) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(String.valueOf(chatId));
        sendMessage.setText(text);
        sendMessage.setReplyMarkup(keyboardMarkup);
        try {
            return absSender.execute(sendMessage).getMessageId();
        } catch (TelegramApiException e) {
            logger.error("Error interacting with Telegram api.");
            return 0;
        }
    }

    private void pwdDialogHandler(AbsSender absSender, User user, String pass) {
        try {
            if(PropertyHelper.getBotUsers().containsKey(user.getName()) &&
               PropertyHelper.getBotUsers().get(user.getName()).equals(pass)) {
                send(absSender, user.getChatId(), Messages.pwdOk);
                send(absSender, user.getChatId(), Messages.reqPath);
                user.setDialogState(User.dialogStates.PATH);
            } else {
                Map.Entry<Boolean, String> tryAnswer = checkTry(user.getDialogState());
                if(!tryAnswer.getKey()) {
                    send(absSender, user.getChatId(), tryAnswer.getValue());
                    user.setDialogState(User.dialogStates.HELLO);
                } else {
                    send(absSender, user.getChatId(), tryAnswer.getValue());
                }
            }
        } catch (PropertyException e) {
            logger.error(e.getMessage());
            send(absSender, user.getChatId(), Messages.prgError);
            user.setDialogState(User.dialogStates.HELLO);
        }
    }

    private void pathDialogHandler(AbsSender absSender, User user, String path) {
        if(PathChecker.isValidPath(path)) {
            user.setPath(path);
            send(absSender, user.getChatId(), Messages.pathOk);
            send(absSender, user.getChatId(), Messages.reqTime, getNotificationButtons());
            user.setDialogState(User.dialogStates.NOTIFICATION);
        } else {
            Map.Entry<Boolean, String> tryAnswer = checkTry(user.getDialogState());
            if(!tryAnswer.getKey()) {
                send(absSender, user.getChatId(), tryAnswer.getValue());
                user.setDialogState(User.dialogStates.HELLO);
            } else {
                send(absSender, user.getChatId(), tryAnswer.getValue());
            }
        }
    }

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

    private InlineKeyboardMarkup getYesNoButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<InlineKeyboardButton>() {{
                    add(InlineKeyboardButton.builder().text("Да").callbackData("yes").build());
                    add(InlineKeyboardButton.builder().text("Нет").callbackData("no").build());
                }})
                .build();
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

    private InlineKeyboardMarkup getStopButtons() {
        return InlineKeyboardMarkup.builder()
                                   .keyboardRow(new ArrayList<InlineKeyboardButton>() {{
                                       add(InlineKeyboardButton.builder().text("Прекратить").callbackData("stop").build());
                                   }})
                                   .build();
    }

    private InlineKeyboardMarkup getChangeButtons() {
        return InlineKeyboardMarkup.builder()
                                   .keyboardRow(new ArrayList<InlineKeyboardButton>() {{
                                       add(InlineKeyboardButton.builder().text("Изменить строку поиска и время").callbackData("path&time").build()); }})
                                   .keyboardRow(new ArrayList<InlineKeyboardButton>() {{
                                       add(InlineKeyboardButton.builder().text("Попрощаться").callbackData("bye").build()); }})
                                   .build();
    }
}
