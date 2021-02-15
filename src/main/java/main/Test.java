package main;

import avitoParser.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Test {
    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String path = "https://www.avito.ru/rossiya?pmax=1000&pmin=100&q=%D0%B2%D0%B8%D0%BD%D0%B8%D0%BB%D0%BE%D0%B2%D1%8B%D0%B9+%D0%BF%D1%80%D0%BE%D0%B8%D0%B3%D1%80%D1%8B%D0%B2%D0%B0%D1%82%D0%B5%D0%BB%D1%8C+audio+technica";

        Method method = Parser.class.getDeclaredMethod("getPages", String.class);
        method.setAccessible(true);
        method.invoke(new Parser(), path);

    }
}
