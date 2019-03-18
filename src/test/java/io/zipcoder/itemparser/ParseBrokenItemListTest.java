package io.zipcoder.itemparser;

import io.zipcoder.utils.Item;
import io.zipcoder.ItemParser;
import io.zipcoder.utils.ItemParseException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class ParseBrokenItemListTest {

    @Test
    public void test1() throws ItemParseException {
        // given
        ItemParser itemParser = new ItemParser();
        String valueToParse = new StringBuilder()
                .append("naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##")
                .append("procrastintion is the assassination of motivation##")
                .append("NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##")
                .toString();
        List<Item> expectedList = Arrays.asList(
                new Item("milk", 3.23, "food", "1/25/2016"),
                new Item("bread", 1.23, "food", "2/25/2016"));

        // when
        List<Item> actualList = itemParser.parseItemList(valueToParse);

        // then
        assertEquals(expectedList, actualList);
    }


    @Test
    public void test2() throws ItemParseException {
        // given
        ItemParser itemParser = new ItemParser();
        String valueToParse = new StringBuilder()
                .append("naMe:Milk;price:3.23;type:Food;expiration:1/25/2016##")
                .append("prepare for the worst##")
                .append("NAMe:BrEAD;price:1.23;type:Food;expiration:2/25/2016##")
                .append("but hope for the best##")
                .append("NAMe:BacoN;price:1.25;type:Food;expiration:2/25/2016##")
                .toString();
        List<Item> expectedList = Arrays.asList(
                new Item("milk", 3.23, "food", "1/25/2016"),
                new Item("bread", 1.23, "food", "2/25/2016"),
                new Item("bacon", 1.25, "food", "2/25/2016"));

        // when
        List<Item> actualList = itemParser.parseItemList(valueToParse);

        // then
        assertEquals(expectedList, actualList);
    }



    @Test
    public void test3() throws ItemParseException {
        // given
        ItemParser itemParser = new ItemParser();
        String valueToParse = new StringBuilder()
                .append("naME:BreaD;price:1.23;type:Food;expiration:1/02/2016##")
                .append("chewy chunky checkered cheddar cheeseburgers##")
                .append("NAMe:eggs;price:1.24;type:Food;expiration:2/25/2016##")
                .append("NAMe:BacoN;price:1.25;type:Food;expiration:2/25/2016##")
                .toString();
        List<Item> expectedList = Arrays.asList(
                new Item("bread", 1.23, "food", "1/02/2016"),
                new Item("eggs", 1.24, "food", "2/25/2016"),
                new Item("bacon", 1.25, "food", "2/25/2016"));

        // when
        List<Item> actualList = itemParser.parseItemList(valueToParse);

        // then
        assertEquals(expectedList, actualList);
    }


}
