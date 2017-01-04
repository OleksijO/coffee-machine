package coffee.machine.controller.command.request.data.extractor.impl;

import coffee.machine.controller.command.request.data.extractor.RefillFormDataExtractor;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author oleksij.onysymchuk@gmail.com
 */
public class RefillFormExtractorImplTest extends AbstractExtractorTest {
    private final RefillFormDataExtractor parser = new RefillFormExtractorImpl();

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

    @Test
    public void testGetAddonsQuantityByIdFromRequestEmptyRequest() throws Exception {
        setupRequestParams(TestData.EMPTY_TEST);
        Assert.assertEquals(
                TestData.EMPTY_TEST.addonsQuantity,
                parser.getAddonsQuantityByIdFromRequest(request));
    }

    @Test
    public void testGetAddonsQuantityByIdFromRequestNonEmptyRequest() throws Exception {
        setupRequestParams(TestData.FULL_TEST);
        Assert.assertEquals(
                TestData.FULL_TEST.addonsQuantity,
                parser.getAddonsQuantityByIdFromRequest(request));
    }

}