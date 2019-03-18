package io.zipcoder;

import io.zipcoder.utils.Item;
import io.zipcoder.utils.ItemParseException;
import jdk.nashorn.internal.runtime.regexp.joni.Regex;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.DoubleUnaryOperator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemParser {
    private Pattern overAllPattern = Pattern.compile("[@|!|:|^|%|*](.*?)[;|#|!|^|%|*|@]");
    private Integer errorCount = 0;

    public Integer getErrorCount() {
        return errorCount;
    }

    public List<Item> parseItemList(String valueToParse) throws ItemParseException {
        Pattern itemPattern = Pattern.compile("(.*?)##");
        Matcher itemMatcher = itemPattern.matcher(valueToParse);
        List<String> items = new ArrayList<>();
        while (itemMatcher.find()) {
            items.add(itemMatcher.group(0));
        }
        List<Item> finalItems = new ArrayList<>();
        for (String item : items) {
            try {
                finalItems.add(parseSingleItem(item));
            } catch (ItemParseException e) {
                errorCount++;
                System.out.println(item);
            }
        }
        return finalItems;
    }


    public Item parseSingleItem(String singleItem) throws ItemParseException {
        try {
        singleItem = singleItem.toLowerCase();
            List<String> matches = new ArrayList<>();
            Matcher matcher = overAllPattern.matcher(singleItem);
            while (matcher.find()) {
                matches.add(matcher.group(0).substring(1, matcher.group(0).length() -1 ));
            }
            String name = matches.get(0).replace("0", "o");
            String priceString = matches.get(1);
            Double price = Double.parseDouble(priceString);
            String type = matches.get(2);
            String expiration = matches.get(3);
            return new Item(name, price, type, expiration);
        } catch (IllegalStateException | NumberFormatException | IndexOutOfBoundsException e) {
            throw new ItemParseException();
        }
    }
}
