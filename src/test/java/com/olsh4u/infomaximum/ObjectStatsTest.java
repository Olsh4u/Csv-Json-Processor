package com.olsh4u.infomaximum;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ObjectStatsTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ObjectStats objectStats;

    @BeforeEach
    public void setUp() {
        objectStats = new ObjectStats();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void testProcessObject() {
        // Test case 1: Process a single object
        objectStats.processObject("Group1", "Type1", BigDecimal.valueOf(5), BigDecimal.valueOf(10));
        Map<String, Integer> duplicates1 = objectStats.getDuplicates();
        assertEquals(1, duplicates1.get("Group: Group1 | Type: Type1"));
        Map<String, BigDecimal> groupWeights1 = objectStats.getGroupWeights();
        assertEquals(BigDecimal.valueOf(10), groupWeights1.get("Group1"));
        assertEquals(BigDecimal.valueOf(10), objectStats.getMinWeight());
        assertEquals(BigDecimal.valueOf(10), objectStats.getMaxWeight());

        // Test case 2: Process duplicate objects
        objectStats.processObject("Group1", "Type1", BigDecimal.valueOf(3), BigDecimal.valueOf(5));
        objectStats.processObject("Group2", "Type2", BigDecimal.valueOf(2), BigDecimal.valueOf(15));
        objectStats.processObject("Group2", "Type2", BigDecimal.valueOf(4), BigDecimal.valueOf(20));
        Map<String, Integer> duplicates2 = objectStats.getDuplicates();
        assertEquals(2, duplicates2.get("Group: Group1 | Type: Type1"));
        assertEquals(2, duplicates2.get("Group: Group2 | Type: Type2"));
        Map<String, BigDecimal> groupWeights2 = objectStats.getGroupWeights();
        assertEquals(BigDecimal.valueOf(35), groupWeights2.get("Group2"));
        assertEquals(BigDecimal.valueOf(5), objectStats.getMinWeight());
        assertEquals(BigDecimal.valueOf(20), objectStats.getMaxWeight());
    }

    @Test
    public void testPrintSummaryStatistics() {
        Map<String, Integer> duplicates = new HashMap<>();
        duplicates.put("Group: Group1 | Type: Type1", 2);
        duplicates.put("Group: Group1 | Type: Type2", 3);
        duplicates.put("Group: Group2 | Type: Type1", 1);
        objectStats.setDuplicates(duplicates);

        Map<String, BigDecimal> groupWeights = new HashMap<>();
        groupWeights.put("Group1", new BigDecimal("10.5"));
        groupWeights.put("Group2", new BigDecimal("15.2"));
        groupWeights.put("Group3", new BigDecimal("7.8"));
        objectStats.setGroupWeights(groupWeights);

        BigDecimal minWeight = new BigDecimal("5.2");
        BigDecimal maxWeight = new BigDecimal("18.6");
        objectStats.setMinWeight(minWeight);
        objectStats.setMaxWeight(maxWeight);

        objectStats.printSummaryStatistics();

        String expectedOutput = "Duplicate objects:\n" +
                "Group: Group1 | Type: Type1 | Duplicates: 2\n" +
                "Group: Group1 | Type: Type2 | Duplicates: 3\n" +
                "Group weights:\n" +
                "Group: Group1 | Weight: 10.5\n" +
                "Group: Group2 | Weight: 15.2\n" +
                "Group: Group3 | Weight: 7.8\n" +
                "Minimum weight: 5.2\n" +
                "Maximum weight: 18.6\n";

        assertEquals(expectedOutput, outputStream.toString());
    }


    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
    }
}




