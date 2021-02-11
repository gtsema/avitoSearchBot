package main;

import avitoParser.Parser;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class Test {

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        String localPage = "src/test/resources/avito292Adverts.htm";

        Method method = Parser.class.getDeclaredMethod("getPages", String.class);
        method.setAccessible(true);
        Object r = method.invoke(new Parser(localPage), localPage);
        System.out.println(((List<String>) r).size());

    }
}
