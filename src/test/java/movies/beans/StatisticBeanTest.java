package movies.beans;

import movies.helpers.FileHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.stream.IntStream;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class StatisticBeanTest {
    @Mock
    FileHelper fileHelperMock;
    @InjectMocks
    StatisticBean statisticBean;

    @Test
    public void putToStatistic() {
        String key = "key";
        int n = 1000;

        IntStream.range(0, n).parallel().forEach(a -> statisticBean.putToStatistic(key));
        statisticBean.postDestroy(new Object());

        List<String> expectedList = List.of(key + " " + n);
        verify(fileHelperMock, times(1)).write(any(), eq(expectedList));
    }

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }
}
