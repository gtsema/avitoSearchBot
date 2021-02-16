package bot;

import dbService.entities.User;
import exceptions.DialogHandlerException;
import exceptions.PropertyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import utils.Messages;
import utils.PathChecker;
import utils.PropertyHelper;

import java.io.Serializable;
import java.util.ArrayList;

public class DialogHandler_ {
    private static final Logger logger = LoggerFactory.getLogger(DialogHandler_.class);

    public BotApiMethod<? extends Serializable> handleUpdate(User user, Update update) throws DialogHandlerException {
        if(update.hasMessage() && !update.getMessage().getText().isEmpty()) {
            return handleMessage(user, update.getMessage());
        } else if(update.hasCallbackQuery()) {
            return handleCallback(user, update.getCallbackQuery());
        } else {
            logger.warn("Unable to identify message type");
        }
        return null;
    }

    private BotApiMethod<? extends Serializable> handleMessage(User user, Message message) throws DialogHandlerException {

        int choiseCounter;

        switch (user.getDialogState()) {
            case HELLO:
                user.setName(message.getFrom().getFirstName());
                return createSendApiMethod(user.getChatId(), String.format(Messages.hello, user.getName()), getYesNoButtons());
            case NAME:
                user.setName(message.getText());
                user.setDialogState(User.dialogStates.PWD);
                return createSendApiMethod(user.getChatId(), String.format(Messages.reqPwd, user.getName()));
            case PWD:
                choiseCounter = user.getCounter();

                if(choiseCounter == 0) {
                    user.setDialogState(User.dialogStates.HELLO);
                    return createSendApiMethod(user.getChatId(), Messages.totalFailure);
                } else if(!checkPwd(user, message.getText())) {
                    return createSendApiMethod(user.getChatId(), Messages.pwdError + Messages.getAttempt(choiseCounter));
                } else {
                    user.resetCounter();
                    user.setDialogState(User.dialogStates.PATH);
                    return createSendApiMethod(user.getChatId(), Messages.reqPath);
                }
            case PATH:
                choiseCounter = user.getCounter();

                if(choiseCounter == 0) {
                    user.setDialogState(User.dialogStates.HELLO);
                    return createSendApiMethod(user.getChatId(), Messages.totalFailure);
                } else if(!PathChecker.isValidPath(message.getText())) {
                    return createSendApiMethod(user.getChatId(), Messages.pathError + Messages.getAttempt(choiseCounter));
                } else {
                    user.resetCounter();
                    user.setDialogState(User.dialogStates.NOTIFICATION);
                    return createSendApiMethod(user.getChatId(), Messages.reqTime, getNotificationButtons());
                }
            case NOTIFICATION:
                return createSendApiMethod(user.getChatId(), "Выберите кнопками, тупица!", getNotificationButtons());
            case READY:
                return createSendApiMethod(user.getChatId(), "Выберите кнопками, тупица!", getYesNoButtons());
            case WORK:
                return createSendApiMethod(user.getChatId(), Messages.stop, getStopButtons());
            case CHANGE:
                return createSendApiMethod(user.getChatId(), "Выберите кнопками, тупица!", getChangeButtons());
            default:
                throw new DialogHandlerException("logic error");
        }
    }

    private BotApiMethod<? extends Serializable> handleCallback(User user, CallbackQuery callbackQuery) throws DialogHandlerException {

        switch (user.getDialogState()) {
            case HELLO:
                if(callbackQuery.getData().equals("yes")) {
                    user.setDialogState(User.dialogStates.PWD);
                    return createSendApiMethod(user.getChatId(), String.format(Messages.reqPwd, user.getName()));
                } else {
                    user.setDialogState(User.dialogStates.NAME);
                    return createSendApiMethod(user.getChatId(), Messages.reqName);
                }
            case NOTIFICATION:
                user.setNotificationTime(Integer.parseInt(callbackQuery.getData()));
                user.setDialogState(User.dialogStates.READY);
                return createSendApiMethod(user.getChatId(), Messages.ok, getYesNoButtons());
            case READY:
                if(callbackQuery.getData().equals("yes")) {
                    //startWork(user);
                    System.out.println("staaaaaaart");
                    user.setDialogState(User.dialogStates.WORK);
                    return createSendApiMethod(user.getChatId(), Messages.addAdverts); // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                } else {
                    user.setDialogState(User.dialogStates.CHANGE);
                    return createSendApiMethod(user.getChatId(), Messages.whatToDo, getChangeButtons());
                }
            case WORK:
                if(callbackQuery.getData().equals("stop")) {
                    user.setDialogState(User.dialogStates.CHANGE);
                    return createSendApiMethod(user.getChatId(), Messages.whatToDo, getChangeButtons());
                } else {
                    return createSendApiMethod(user.getChatId(), Messages.stop, getStopButtons()); // <<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
                }
            case CHANGE:
                if(callbackQuery.getData().equals("path&time")) {
                    user.setDialogState(User.dialogStates.PATH);
                    return createSendApiMethod(user.getChatId(), Messages.reqPath);
                } else {
                    user.setDialogState(User.dialogStates.HELLO);
                    return createSendApiMethod(user.getChatId(), String.format(Messages.bye, user.getName()));
                }
            default:
                throw new DialogHandlerException("logic error");
        }
    }

    //------------------------------------------------------------------------------------------------------------------

    private SendMessage createSendApiMethod(long chatId, String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(text);
        return message;
    }

    private SendMessage createSendApiMethod(long chatId, String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = createSendApiMethod(chatId, text);
        message.setReplyMarkup(keyboard);
        return message;
    }

    //------------------------------------------------------------------------------------------------------------------

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

    private InlineKeyboardMarkup getChangeButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Изменить строку поиска и время").callbackData("path&time").build());
                }})
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Попрощаться").callbackData("bye").build()); }})
                .build();
    }

    private InlineKeyboardMarkup getStopButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Прекратить").callbackData("stop").build());
                }})
                .build();
    }

    //------------------------------------------------------------------------------------------------------------------

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

