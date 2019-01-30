package com.example.unamed.mvc.error;

public class NullChecker {

    /**
     * If one parameter is null throw {@link NullPointerException}.
     */
    public static void isNotNullE(Object... objects) throws NullPointerException {
        for (Object object : objects) {
            if (object == null) {
                throw new NullPointerException("Exception!");
            }
        }
    }

    /**
     * If one parameter is null return false.
     */
    public static boolean isNotNullB(Object... objects) {
        for (Object object : objects) {
            if (object == null) {
                return false;
            }
        }
        return true;
    }

}
