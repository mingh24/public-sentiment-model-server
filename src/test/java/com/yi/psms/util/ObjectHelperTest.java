package com.yi.psms.util;

import com.yi.psms.model.vo.opinion.OpinionCountVO;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ObjectHelperTest {

    @Test
    void buildObjectFromMap() {
        Map<String, Object> m1 = new HashMap<>();
        m1.put("name", "test1");
        m1.put("count", 123);
        var o1 = ObjectHelper.buildObjectFromMap(m1, OpinionCountVO.class);
        assertEquals(new OpinionCountVO("test1", 123), o1);

        Map<String, Object> m2 = new HashMap<>();
        m2.put("name", "test2");
        m2.put("count", null);
        var o2 = ObjectHelper.buildObjectFromMap(m2, OpinionCountVO.class);
        assertEquals(new OpinionCountVO("test2", null), o2);

        Map<String, Object> m3 = new HashMap<>();
        m3.put("name", "test3");
        var o3 = ObjectHelper.buildObjectFromMap(m3, OpinionCountVO.class);
        assertEquals(new OpinionCountVO("test3", null), o3);

        Map<String, Object> m4 = new HashMap<>();
        m4.put("name", "test4");
        m4.put("otherField", 0);
        var o4 = ObjectHelper.buildObjectFromMap(m4, OpinionCountVO.class);
        assertEquals(new OpinionCountVO("test4", null), o4);
    }

}