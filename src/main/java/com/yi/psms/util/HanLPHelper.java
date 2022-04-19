package com.yi.psms.util;

import com.hankcs.hanlp.dictionary.stopword.CoreStopWordDictionary;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.BasicTokenizer;
import lombok.val;

import java.util.ArrayList;
import java.util.List;

public class HanLPHelper {

    public static List<Term> rawSegment(String text) {
        return BasicTokenizer.segment(text);
    }

    public static List<Term> rawSegmentStopWordRemoved(String text) {
        return CoreStopWordDictionary.apply(rawSegment(text));
    }

    public static List<String> segment(String text) {
        List<String> res = new ArrayList<>();
        var segmentationList = rawSegment(text);

        for (val s : segmentationList) {
            res.add(s.word);
        }

        return res;
    }

    public static List<String> segmentStopWordRemoved(String text) {
        List<String> res = new ArrayList<>();
        var segmentationList = rawSegmentStopWordRemoved(text);

        for (val s : segmentationList) {
            res.add(s.word);
        }

        return res;
    }

}
