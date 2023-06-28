package com.olsh4u.infomaximum;


import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class ObjectStats {
    private  Map<String, Integer> duplicates;
    private  Map<String, BigDecimal> groupWeights;
    private BigDecimal minWeight;
    private BigDecimal maxWeight;
    protected static Long countOfElements = 0L;

    private boolean isShouldPrinted = true;

    public ObjectStats() {
        duplicates = new HashMap<>();
        groupWeights = new HashMap<>();
        minWeight = BigDecimal.valueOf(Long.MAX_VALUE);
        maxWeight = BigDecimal.valueOf(Long.MIN_VALUE);
    }

    protected void processObject(String group, String type, BigDecimal number, BigDecimal weight) {
        String key = "Group: " + group + " | Type: " + type;
        duplicates.merge(key, 1, Integer::sum);
        groupWeights.compute(group, (k, v) -> (v == null) ? weight : v.add(weight));
        minWeight = minWeight.min(weight);
        maxWeight = maxWeight.max(weight);
    }

    protected void printSummaryStatistics() {
        if (countOfElements > 10000000) {
            System.out.println("file cannot contain more than 10_000_000 lines");
            return;
        }
        countOfElements = 0L;
        System.out.println("Duplicate objects:");
        for (Map.Entry<String, Integer> entry : duplicates.entrySet()) {
            if (entry.getValue() > 1) {
                System.out.println(entry.getKey() + " | Duplicates: " + entry.getValue());
            }
        }

        System.out.println("Group weights:");
        for (Map.Entry<String, BigDecimal> entry : groupWeights.entrySet()) {
            System.out.println("Group: " + entry.getKey() + " | Weight: " + entry.getValue());
        }

        System.out.println("Minimum weight: " + minWeight);
        System.out.println("Maximum weight: " + maxWeight);
    }


    public Map<String, Integer> getDuplicates() {
        return duplicates;
    }

    public void setDuplicates(Map<String, Integer> duplicates) {
        this.duplicates = duplicates;
    }

    public Map<String, BigDecimal> getGroupWeights() {
        return groupWeights;
    }

    public void setGroupWeights(Map<String, BigDecimal> groupWeights) {
        this.groupWeights = groupWeights;
    }

    public BigDecimal getMinWeight() {
        return minWeight;
    }

    public void setMinWeight(BigDecimal minWeight) {
        this.minWeight = minWeight;
    }

    public BigDecimal getMaxWeight() {
        return maxWeight;
    }

    public void setMaxWeight(BigDecimal maxWeight) {
        this.maxWeight = maxWeight;
    }

    public boolean isShouldPrinted() {
        return isShouldPrinted;
    }

    public void setShouldPrinted(boolean shouldPrinted) {
        isShouldPrinted = shouldPrinted;
    }

    public static Long getCountOfElements() {
        return countOfElements;
    }

    public static void setCountOfElements(Long countOfElements) {
        ObjectStats.countOfElements = countOfElements;
    }
}

