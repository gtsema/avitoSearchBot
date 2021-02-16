package main;

import java.lang.reflect.InvocationTargetException;

public class Test {

    public static int choiseCounter = 3;

    public static void main(String[] args) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        while (true) {
            System.out.println(getChoise());
        }
    }

    public static int getChoise() {
        return choiseCounter != 0 ? --choiseCounter : (choiseCounter = 3);
    }
}
