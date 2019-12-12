package movies.beans;

import movies.helpers.FileHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class StatisticBeanTest {
    @Mock
    FileHelper fileHelperMock;
    @InjectMocks
    StatisticBean statisticBean;

    @Test
    public void putToStatistic() {
        String key = "GET path";
        int n = 1000;

        IntStream.range(0, n).parallel().forEach(a -> statisticBean.putToStatistic(key));
        statisticBean.postDestroy(new Object());

        List<String> expectedList = List.of(key + " " + n);
        verify(fileHelperMock, times(1)).write(any(), eq(expectedList));
    }

    @Test
    public void postConstruct() {
        String key = "GET path";
        int n = 10;

        List<String> linesInFile = List.of(key + " " + n);

        when(fileHelperMock.exists(any())).thenReturn(true);
        when(fileHelperMock.lines(any())).thenReturn(linesInFile.stream());

        statisticBean.postConstruct(new Object());
        assertEquals(n, statisticBean.getCountForKey(key));
    }

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }
}
