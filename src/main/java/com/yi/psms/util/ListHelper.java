package com.yi.psms.util;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ListHelper {

    public static <E, K> List<E> extractDuplicateElements(List<E> list, Function<E, K> keyExtractor) {
        var duplicateMap = list.stream()
                .collect(Collectors.toMap(keyExtractor, e -> 1, Integer::sum));
        return list.stream()
                .filter(e -> duplicateMap.get(keyExtractor.apply(e)) > 1)
                .collect(Collectors.toList());
    }

}
