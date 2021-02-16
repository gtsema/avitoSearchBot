package dbService.entities;

import java.util.Date;
import java.util.Objects;

public class User {
    public enum dialogStates {HELLO, NAME, PWD, PATH, NOTIFICATION, READY, WORK, CHANGE};

    private long chatId;
    private String name;
    private String path;
    private int notificationTime;
    private Date timeOfLastVisits;
    private dialogStates dialogState;
    private int choiceCounter = 3;

    public User(long chatId) {
        this.chatId = chatId;
        dialogState = dialogStates.HELLO;
    }

    public int getCounter() {
        return choiceCounter != 0 ? --choiceCounter : (choiceCounter = 2);
    }

    public void resetCounter() {
        choiceCounter = 3;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setNotificationTime(int notificationTime) {
        this.notificationTime = notificationTime;
    }

    public void setTimeOfLastVisits(Date timeOfLastVisits) {
        this.timeOfLastVisits = timeOfLastVisits;
    }

    public void setDialogState(dialogStates dialogState) {
        this.dialogState = dialogState;
    }

    public long getChatId() {
        return chatId;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public int getNotificationTime() {
        return notificationTime;
    }

    public Date getTimeOfLastVisits() {
        return timeOfLastVisits;
    }

    public dialogStates getDialogState() {
        return dialogState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return chatId == user.chatId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(chatId);
    }
}
