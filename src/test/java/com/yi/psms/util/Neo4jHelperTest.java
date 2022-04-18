package com.yi.psms.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Neo4jHelperTest {

    @Test
    void buildPriceOptionString() {
        assertEquals("A@15-30元/小时", Neo4jHelper.buildPriceOptionString("A", "15-30元/小时"));
        assertEquals("B@null", Neo4jHelper.buildPriceOptionString("B", null));
    }

    @Test
    void parsePriceOptionString() {
        assertArrayEquals(new String[]{"A", "15-30元/小时"}, Neo4jHelper.parsePriceOptionString("A@15-30元/小时"));
        assertArrayEquals(new String[]{"B"}, Neo4jHelper.parsePriceOptionString("B@"));
        assertArrayEquals(new String[]{"C"}, Neo4jHelper.parsePriceOptionString("C"));
        assertArrayEquals(new String[]{""}, Neo4jHelper.parsePriceOptionString(""));
        assertArrayEquals(new String[]{}, Neo4jHelper.parsePriceOptionString("@"));
    }

    @Test
    void buildLengthOptionString() {
        assertEquals("A@0.5小时", Neo4jHelper.buildLengthOptionString("A", "0.5小时"));
        assertEquals("B@null", Neo4jHelper.buildLengthOptionString("B", null));
    }

    @Test
    void parseLengthOptionString() {
        assertArrayEquals(new String[]{"A", "0.5小时"}, Neo4jHelper.parseLengthOptionString("A@0.5小时"));
        assertArrayEquals(new String[]{"B"}, Neo4jHelper.parseLengthOptionString("B@"));
        assertArrayEquals(new String[]{"C"}, Neo4jHelper.parseLengthOptionString("C"));
        assertArrayEquals(new String[]{""}, Neo4jHelper.parseLengthOptionString(""));
        assertArrayEquals(new String[]{}, Neo4jHelper.parseLengthOptionString("@"));
    }

}