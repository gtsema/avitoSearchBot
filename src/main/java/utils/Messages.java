package utils;

import java.util.HashMap;
import java.util.Map;

public class Messages {

    public final static String hello = "Здравствуйте, %s! Могу ли я к Вам так обращаться?";
    public final static String reqName = "Тогда введите своё имя:";
    public final static String reqPwd = "%s, введите свой пароль:";
    public final static String pwdOk = "Пароль верный!";
    public final static String pwdError = "Пароль не подходит. Попробуйте ещё раз.";
    public final static String reqPath = "Теперь введите строку поиска:";
    public final static String pathError = "Не могу найти объявления на этой старнице. Попробуйте ещё раз.";
    public final static String pathOk = "У Вас отлично получается!";
    public final static String reqTime = "Как часто искать новые объявления?";
    public final static String notificationOk = "Период поиска установлен.";
    public final static String ok = "Всё готово. Приступать к работе?";
    public final static String totalFailure = "Попытки исчерпаны. До свидания.";
    public final static String prgError = "Произошла ошибка и дальнейшая работа невозможна. До свидания!";
    public final static String whatToDo = "%s, что вы хотите сделать?";
    public final static String bye = "До свидания, %s!";
    public final static String stop = "Хотите всё это прекратить?";


    private final static Map<Integer, String> attempt  = new HashMap<>() {{
        put(1, " Осталась одна попытка.");
        put(2, " Осталось две попытки.");
        put(3, " Осталось три попытки.");
    }};

    public static String getAttempt(int key) {
        return attempt.get(key);
    }
}
