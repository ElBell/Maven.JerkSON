package io.zipcoder;

import io.zipcoder.utils.FileReader;
import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import java.util.*;

public class GroceryReporter {
    private final String originalFileText;
    private List<Item> items;
    private Map<String, Map<Double, Integer>> counts;
    private Map<String, Integer> itemCounts;
    private StringBuilder stringBuilder;
    private final List<String> VALID_ITEMS = Arrays.asList("milk", "bread", "cookies", "apples");

    public GroceryReporter(String jerksonFileName) {
        this.originalFileText = FileReader.readFile(jerksonFileName);
        counts = new HashMap<>();
        itemCounts = new HashMap<>();
        for (String valid_item : VALID_ITEMS) {
            itemCounts.put(valid_item, 0);
        }
        itemCounts.put("Error", 0);
        stringBuilder = new StringBuilder();
        getList();
        getCounts();
    }

    @Override
    public String toString() {
        for (String itemName : VALID_ITEMS) {
            addName(itemName);
            addPrices(itemName);
        }
        stringBuilder.append(String.format("Errors         \t \t seen: %d times\n", itemCounts.get("Error")));
        return stringBuilder.toString();
    }

    private void addName(String itemName) {
        String caps = itemName.substring(0, 1).toUpperCase() + itemName.substring(1);
        stringBuilder.append("name:").append(String.format("%8s", caps));
        stringBuilder.append("\t\t ").append(String.format("seen: %d times\n", itemCounts.get(itemName)));
        stringBuilder.append("============= \t \t =============\n");
    }

    private void addPrices(String itemName) {
        int count = 0;
        for (Double price: counts.get(itemName).keySet()) {
            if (counts.get(itemName).get(price) > 1) {
                stringBuilder.append(String.format("Price: 	 %.2f		 seen: %d times\n", price, counts.get(itemName).get(price)));
            } else {
                stringBuilder.append(String.format("Price: 	 %.2f		 seen: %d time\n", price, counts.get(itemName).get(price)));
            }
                if(count++ == 0) {stringBuilder.append("-------------		 -------------\n"); }
        }
        stringBuilder.append("\n");
    }

    public void getList() {
        ItemParser itemParser = new ItemParser();
        try {
            items = itemParser.parseItemList(originalFileText);
            for (Integer integer = 0; integer < itemParser.getErrorCount(); integer++) {
                errorCount();
            }
        } catch (ItemParseException e) {
            e.printStackTrace();
        }
    }

    public void errorCount() {
        itemCounts.put("Error", itemCounts.get("Error") + 1);
    }

    public void getCounts() {
        for (Item item : items) {
            if(!VALID_ITEMS.contains(item.getName())) {
                errorCount();
            } else {
                if (counts.containsKey(item.getName())) {
                    counts.put(item.getName(), updateItem(item));
                } else {
                    Map<Double, Integer> newPrice = new HashMap<>();
                    newPrice.put(item.getPrice(), 1);
                    itemCounts.put(item.getName(), 1);
                    counts.put(item.getName(), newPrice);
                }
            }
        }

    }

    private Map<Double, Integer> updateItem(Item item) {
        itemCounts.put(item.getName(), itemCounts.get(item.getName()) + 1);
        Map<Double, Integer> tempMap = counts.get(item.getName());
        if (tempMap.containsKey(item.getPrice())) {
            tempMap.put(item.getPrice(), tempMap.get(item.getPrice()) + 1);
        } else {
            tempMap.put(item.getPrice(), 1);
        }
        return tempMap;
    }
}
