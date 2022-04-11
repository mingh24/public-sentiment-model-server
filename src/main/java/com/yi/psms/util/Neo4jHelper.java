package com.yi.psms.util;

import lombok.val;
import org.neo4j.driver.internal.value.MapValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Neo4jHelper {

    public static String buildPriceOptionString(String optionKey, String optionValue) {
        return String.format("%s@%s", optionKey, optionValue);
    }

    public static String[] parsePriceOptionString(String option) {
        return option.split("@");
    }

    public static String buildLengthOptionString(String optionKey, String optionValue) {
        return String.format("%s@%s", optionKey, optionValue);
    }

    public static String[] parseLengthOptionString(String option) {
        return option.split("@");
    }

    public static Map<String, Object> parseRawMapValue(MapValue mapValue) {
        return mapValue.asMap();
    }

    public static List<Map<String, Object>> parseRawMapValueList(List<MapValue> mapValueList) {
        List<Map<String, Object>> res = new ArrayList<>();

        for (val mapValue : mapValueList) {
            res.add(parseRawMapValue(mapValue));
        }

        return res;
    }

}
