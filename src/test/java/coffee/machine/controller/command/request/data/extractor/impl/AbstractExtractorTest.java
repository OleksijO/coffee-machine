package coffee.machine.controller.command.request.data.extractor.impl;

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
 * @author oleksij.onysymchuk@gmail.com
 */
abstract class AbstractExtractorTest {

    @Mock
    protected HttpServletRequest request;


    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    /**
     * Prepares request parameters according to data in param
     *
     * @param testData One of prepared data for tests
     */
    protected void setupRequestParams(TestData testData) {
        Enumeration<String> params = new Enumeration<String>() {
            Iterator<String> iterator = testData.requestParams.keySet().iterator();

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
                return testData.requestParams.get(args[0]);
            }
        });
    }
}
