package coffee.machine.controller.impl.command.request.data.extractor.impl;

import coffee.machine.controller.impl.command.request.data.extractor.PurchaseFormDataExtractor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class PurchaseFormExtractorImplTest extends AbstractExtractorTest {
    private final PurchaseFormDataExtractor parser = new PurchaseFormExtractorImpl();

    @Test
    public void testGetAddonsQuantityInDrinksByIdFromRequestNonEmptyRequest() throws Exception {
        setupRequestParams(TestData.FULL_TEST);
        Assert.assertEquals(
                TestData.FULL_TEST.addonsByDrinkIdQuantity,
                parser.getAddonsQuantityInDrinksByIdFromRequest(request));
    }

    @Test
    public void testGetAddonsQuantityInDrinksByIdFromRequestEmptyRequest() throws Exception {
        setupRequestParams(TestData.EMPTY_TEST);
        Assert.assertEquals(
                TestData.EMPTY_TEST.addonsByDrinkIdQuantity,
                parser.getAddonsQuantityInDrinksByIdFromRequest(request));
    }

    @Test
    public void testGetDrinksQuantityByIdFromRequestEmptyRequest() throws Exception {
        setupRequestParams(TestData.EMPTY_TEST);
        Assert.assertEquals(
                TestData.EMPTY_TEST.drinksQuantity,
                parser.getDrinksQuantityByIdFromRequest(request));
    }

    @Test
    public void testGetDrinksQuantityByIdFromRequestNonEmptyRequest() throws Exception {
        setupRequestParams(TestData.FULL_TEST);
        Assert.assertEquals(
                TestData.FULL_TEST.drinksQuantity,
                parser.getDrinksQuantityByIdFromRequest(request));
    }

}