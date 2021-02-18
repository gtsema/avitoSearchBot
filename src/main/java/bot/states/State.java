package bot.states;

import bot.Bot;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.DeleteMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.Serializable;
import java.util.ArrayList;

public abstract class State {

    static int prevMessageId;
    Bot bot;

    public State(Bot bot) {
        this.bot = bot;
    }

    public abstract void handleMessage(Message message);

    public abstract void handleCallback(CallbackQuery callback);

    public void handleUpdate(Update update) {
        if(update.hasMessage() && !update.getMessage().getText().isEmpty()) {
            handleMessage(update.getMessage());
        } else if(update.hasCallbackQuery()) {
            if(update.getCallbackQuery().getMessage().getMessageId() == prevMessageId) {
                deleteMarkup(update.getCallbackQuery().getMessage().getMessageId());
                handleCallback(update.getCallbackQuery());
            }
        }
    }


    public void sendMessage(String text) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(bot.getUser().getChatId()));
        message.setText(text);
        executeApiMethod(message);
    }

    public void sendMessage(String text, InlineKeyboardMarkup keyboard) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(bot.getUser().getChatId()));
        message.setText(text);
        message.setReplyMarkup(keyboard);
        executeApiMethod(message);
    }

    public void deleteMessage(int messageId) {
        DeleteMessage message = new DeleteMessage();
        message.setChatId(String.valueOf(bot.getUser().getChatId()));
        message.setMessageId(messageId);
        executeApiMethod(message);
    }

    public void deleteMarkup(int messageId) {
        EditMessageReplyMarkup markup = new EditMessageReplyMarkup();
        markup.setChatId(String.valueOf(bot.getUser().getChatId()));
        markup.setMessageId(messageId);
        executeApiMethod(markup);
    }

    private void executeApiMethod(BotApiMethod<? extends Serializable> method) {
        try {
            if(method.getMethod().equals("sendmessage") && ((SendMessage)method).getReplyMarkup() != null) {
                prevMessageId = bot.execute((SendMessage)method).getMessageId();
            } else {
                bot.execute(method);
            }
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }


    InlineKeyboardMarkup getYesNoButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Да").callbackData("yes").build());
                    add(InlineKeyboardButton.builder().text("Нет").callbackData("no").build());
                }})
                .build();
    }

    InlineKeyboardMarkup getNotificationButtons() {
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

    InlineKeyboardMarkup getStopButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Прекратить").callbackData("stop").build());
                }})
                .build();
    }

    InlineKeyboardMarkup getChangeButtons() {
        return InlineKeyboardMarkup.builder()
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Изменить строку поиска и время").callbackData("path&time").build());
                }})
                .keyboardRow(new ArrayList<>() {{
                    add(InlineKeyboardButton.builder().text("Попрощаться").callbackData("bye").build()); }})
                .build();
    }

}
