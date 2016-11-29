package coffee_machine.controller.impl.command.parser.impl;

import org.junit.Before;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.Iterator;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by oleksij.onysymchuk@gmail on 29.11.2016.
 */
abstract class AbstractParserTest {

    @Mock
    protected HttpServletRequest request;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

   protected void setupRequestParams(TestData fullTest) {
        Enumeration<String> params = new Enumeration<String>() {
            Iterator<String> iterator = fullTest.requestParams.keySet().iterator();

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
                return fullTest.requestParams.get(args[0]);
            }
        });
    }
}
