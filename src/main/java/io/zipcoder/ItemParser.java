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
    private Pattern namePattern = Pattern.compile("name(.*?)price");
    private Pattern pricePattern = Pattern.compile("price(.*?)type");
    private Pattern typePattern = Pattern.compile("type(.*?)expiration");
    private Pattern expirationPattern = Pattern.compile("expiration(.*?)#");
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
            }
        }
        return finalItems;
    }

    public Item parseSingleItem(String singleItem) throws ItemParseException {
        singleItem = singleItem.toLowerCase().replaceAll("co0kies", "cookies");
        try {
            Matcher nameMatcher = namePattern.matcher(singleItem);
            Matcher priceMatcher = pricePattern.matcher(singleItem);
            Matcher typeMatcher = typePattern.matcher(singleItem);
            Matcher expirationMatcher = expirationPattern.matcher(singleItem);
            nameMatcher.find();
            typeMatcher.find();
            priceMatcher.find();
            expirationMatcher.find();
            String name = sub(nameMatcher.group(1).toLowerCase());
            String priceString = sub(priceMatcher.group(1));
            Double price = Double.parseDouble(priceString);
            String type = sub(typeMatcher.group(1).toLowerCase());
            String expiration = expirationMatcher.group(1).substring(1);
            return new Item(name, price, type, expiration);
        } catch (IllegalStateException| StringIndexOutOfBoundsException | NumberFormatException e) {
            throw new ItemParseException();
        }
    }

    private String sub(String string) {
        return string.substring(1, string.length() - 1);
    }
}
