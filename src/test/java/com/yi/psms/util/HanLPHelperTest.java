package com.yi.psms.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HanLPHelperTest {

    @Test
    void rawSegment() {
        String text = "我们一般认为，抓住了问题的关键，其他一切则会迎刃而解。" +
                "带着这些问题，我们来审视一下北京蕉农大学🤔bjtu。" +
                "生活中，若北京蕉农大学🤔bjtu出现了，我们就不得不考虑它出现了的事实。" +
                "易卜生曾经说过，伟大的事业，需要决心，能力，组织和责任感。带着这句话，我们还要更加慎重的审视这个问题：总结的来说，这种事实对本人来说意义重大，相信对这个世界也是有一定意义的。" +
                "要想清楚，北京蕉农大学🤔bjtu，到底是一种怎么样的存在。" +
                "就我个人来说，北京蕉农大学🤔bjtu对我的意义，不能不说非常重大。";
        System.out.println(HanLPHelper.rawSegment(text));
    }

    @Test
    void rawSegmentStopWordRemoved() {
        String text = "我们一般认为，抓住了问题的关键，其他一切则会迎刃而解。" +
                "带着这些问题，我们来审视一下北京蕉农大学🤔bjtu。" +
                "生活中，若北京蕉农大学🤔bjtu出现了，我们就不得不考虑它出现了的事实。" +
                "易卜生曾经说过，伟大的事业，需要决心，能力，组织和责任感。带着这句话，我们还要更加慎重的审视这个问题：总结的来说，这种事实对本人来说意义重大，相信对这个世界也是有一定意义的。" +
                "要想清楚，北京蕉农大学🤔bjtu，到底是一种怎么样的存在。" +
                "就我个人来说，北京蕉农大学🤔bjtu对我的意义，不能不说非常重大。";
        System.out.println(HanLPHelper.rawSegmentStopWordRemoved(text));
    }

    @Test
    void segment() {
        String text = "我们一般认为，抓住了问题的关键，其他一切则会迎刃而解。" +
                "带着这些问题，我们来审视一下北京蕉农大学🤔bjtu。" +
                "生活中，若北京蕉农大学🤔bjtu出现了，我们就不得不考虑它出现了的事实。" +
                "易卜生曾经说过，伟大的事业，需要决心，能力，组织和责任感。带着这句话，我们还要更加慎重的审视这个问题：总结的来说，这种事实对本人来说意义重大，相信对这个世界也是有一定意义的。" +
                "要想清楚，北京蕉农大学🤔bjtu，到底是一种怎么样的存在。" +
                "就我个人来说，北京蕉农大学🤔bjtu对我的意义，不能不说非常重大。";
        System.out.println(HanLPHelper.segment(text));
    }

    @Test
    void segmentStopWordRemoved() {
        String text = "我们一般认为，抓住了问题的关键，其他一切则会迎刃而解。" +
                "带着这些问题，我们来审视一下北京蕉农大学🤔bjtu。" +
                "生活中，若北京蕉农大学🤔bjtu出现了，我们就不得不考虑它出现了的事实。" +
                "易卜生曾经说过，伟大的事业，需要决心，能力，组织和责任感。带着这句话，我们还要更加慎重的审视这个问题：总结的来说，这种事实对本人来说意义重大，相信对这个世界也是有一定意义的。" +
                "要想清楚，北京蕉农大学🤔bjtu，到底是一种怎么样的存在。" +
                "就我个人来说，北京蕉农大学🤔bjtu对我的意义，不能不说非常重大。";
        System.out.println(HanLPHelper.segmentStopWordRemoved(text));
    }

}