package com.yi.psms.util;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;

public class ObjectHelper {

    public static <T> T buildObjectFromMap(Map<String, Object> map, Class<T> T) {
        Gson gson = new Gson();
        JsonElement jsonElement = gson.toJsonTree(map);
        return gson.fromJson(jsonElement, T);
    }

    public static <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Set<Object> seen = ConcurrentHashMap.newKeySet();
        return t -> seen.add(keyExtractor.apply(t));
    }

}
