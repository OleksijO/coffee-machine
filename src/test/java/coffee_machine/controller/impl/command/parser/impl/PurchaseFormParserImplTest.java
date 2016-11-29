package coffee_machine.controller.impl.command.parser.impl;

import coffee_machine.controller.impl.command.parser.PurchaseFormParser;
import coffee_machine.view.Parameters;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
public class PurchaseFormParserImplTest {
    @Mock
    private HttpServletRequest request;

    private Map<String, String> requestParams = new HashMap<String, String>() {{
        int drinkId = 1;
        String drink = Parameters.DRINK_PARAMETER_STARTS_WITH;
        String addon = Parameters.ADDON_PARAMETER_STARTS_WITH;
        put(drink + drinkId, "0");
        put(drink + drinkId + addon + 1, "0");
        put(drink + drinkId++ + addon + 2, "1");
        put(drink + drinkId, "1");
        put(drink + drinkId + addon + 1, "0");
        put(drink + drinkId++ + addon + 2, "0");
        put(drink + drinkId, "1");
        put(drink + drinkId + addon + 1, "1");
        put(drink + drinkId++ + addon + 2, "0");
        put(drink + drinkId, "1");
        put(drink + drinkId + addon + 1, "0");
        put(drink + drinkId++ + addon + 2, "1");
        put(drink + drinkId, "1");
        put(drink + drinkId + addon + 1, "2");
        put(drink + drinkId + addon + 2, "2");

    }};

    private Map<Integer, Map<Integer,Integer>> gauge = new HashMap<Integer, Map<Integer,Integer>>(){{
        put(1,new HashMap<Integer,Integer>(){{put(2,1);}});
        put(3,new HashMap<Integer,Integer>(){{put(1,1);}});
        put(4,new HashMap<Integer,Integer>(){{put(2,1);}});
        put(5,new HashMap<Integer,Integer>(){{put(1,2);put(2,2);}});
    }};

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);

        Enumeration<String> params = new Enumeration<String>() {
            Iterator<String> iterator = requestParams.keySet().iterator();

            @Override
            public boolean hasMoreElements() {
                return iterator.hasNext();
            }

            @Override
            public String nextElement() {
                return iterator.next();
            }
        };
        when(request.getParameterNames()).thenReturn(params);

        when(request.getParameter(anyString())).thenAnswer(new Answer<String>() {
            @Override
            public String answer(InvocationOnMock invocation) throws Throwable {
                Object[] args = invocation.getArguments();
                return requestParams.get(args[0]);
            }
        });
    }



    @Test
    public void testGetAddonsQuantityInDrinksByIdFromRequest() throws Exception {
        PurchaseFormParser parser=new PurchaseFormParserImpl();
        Map<Integer, Map<Integer,Integer>> addonsQuantity = parser.getAddonsQuantityInDrinksByIdFromRequest(request);
        Assert.assertEquals(gauge, addonsQuantity);
    }

}