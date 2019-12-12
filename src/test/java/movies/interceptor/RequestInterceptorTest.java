package movies.interceptor;

import movies.beans.StatisticBean;
import movies.interceptors.RequestInterceptor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.interceptor.InvocationContext;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.reflect.Method;

import static org.mockito.Mockito.*;

public class RequestInterceptorTest {

    @Mock
    StatisticBean statServiceMock;
    @InjectMocks
    RequestInterceptor requestInterceptor;

    @Test
    void processRequest_methodWithoutPath() throws Exception {
        InvocationContext mockContext = mock(InvocationContext.class);

        Method testMethod = TestClass.class.getMethod("testGetMethodWithoutPath");

        when(mockContext.getMethod()).thenReturn(testMethod);

        requestInterceptor.processRequest(mockContext);
        verify(statServiceMock, times(1)).putToStatistic("GET path");
    }

    @Test
    void processRequest_methodWithPath() throws Exception {
        InvocationContext mockContext = mock(InvocationContext.class);

        Method testMethod = TestClass.class.getMethod("testPostMethodWithPath");

        when(mockContext.getMethod()).thenReturn(testMethod);

        requestInterceptor.processRequest(mockContext);
        verify(statServiceMock, times(1)).putToStatistic("POST path/subpath");
    }

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }

    @Path("path")
    private class TestClass {
        @GET
        public void testGetMethodWithoutPath() {
        }

        @POST
        @Path("subpath")
        public void testPostMethodWithPath() {
        }
    }
}
