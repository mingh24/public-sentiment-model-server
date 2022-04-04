package com.yi.psms.util;

public class Neo4jHelper {

    public static String buildPriceOptionString(String optionKey, String optionValue) {
        return String.format("%s@%s", optionKey, optionValue);
    }

    public static String buildLengthOptionString(String optionKey, String optionValue) {
        return String.format("%s@%s", optionKey, optionValue);
    }

}
