package coffee_machine.controller.impl.command.parser.impl;

import coffee_machine.controller.impl.command.parser.RefillFormParser;
import org.junit.Assert;
import org.junit.Test;

import static coffee_machine.controller.impl.command.parser.impl.TestData.EMPTY_TEST;
import static coffee_machine.controller.impl.command.parser.impl.TestData.FULL_TEST;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
public class RefillFormParserImplTest extends AbstractParserTest {
    private final RefillFormParser parser = new RefillFormParserImpl();

    @Test
    public void testGetDrinksQuantityByIdFromRequestEmptyRequest() throws Exception {
        setupRequestParams(EMPTY_TEST);
        Assert.assertEquals(
                EMPTY_TEST.drinksQuantity,
                parser.getDrinksQuantityByIdFromRequest(request));
    }

    @Test
    public void testGetDrinksQuantityByIdFromRequestNonEmptyRequest() throws Exception {
        setupRequestParams(FULL_TEST);
        Assert.assertEquals(
                FULL_TEST.drinksQuantity,
                parser.getDrinksQuantityByIdFromRequest(request));
    }

    @Test
    public void testGetAddonsQuantityByIdFromRequestEmptyRequest() throws Exception {
        setupRequestParams(EMPTY_TEST);
        Assert.assertEquals(
                EMPTY_TEST.addonsQuantity,
                parser.getAddonsQuantityByIdFromRequest(request));
    }

    @Test
    public void testGetAddonsQuantityByIdFromRequestNonEmptyRequest() throws Exception {
        setupRequestParams(FULL_TEST);
        Assert.assertEquals(
                FULL_TEST.addonsQuantity,
                parser.getAddonsQuantityByIdFromRequest(request));
    }

}