package utils;

import java.util.HashMap;
import java.util.Map;

public class Messages {
    private final static Map<String, String> messages = new HashMap<>() {{
        put("Hello", "Здравствуйте, %s! Введите свой пароль.");
        put("PassOk", "Пароль верный. Теперь нужно ввести строку поиска.");
        put("Fail", "Что-то не так. Попробуй ещё раз.");
        put("TotalFailure", "Попытки исчерпаны. До свидания.");
        put("Error", "Произошла ошибка и дальнейшая работа невозможна. До свидания!");
    }};

    private final static Map<Integer, String> choiseString = new HashMap<>() {{
        put(1, " Осталась одна попытка.");
        put(2, " Осталось две попытки.");
        put(3, " Осталось три попытки.");
    }};

    public static String getMessage(String key) {
        return messages.get(key);
    }

    public static String getChoise(int key) {
        return choiseString.get(key);
    }
}
