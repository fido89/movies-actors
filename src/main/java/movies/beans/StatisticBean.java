package movies.beans;

import movies.helpers.FileHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class StatisticBean {

    @Inject
    FileHelper fileHelper;

    private Map<String, AtomicInteger> map = new HashMap<>();
    private Path path = Path.of("./data/stats.txt");

    public void putToStatistic(String key) {
        AtomicInteger counter = map.computeIfAbsent(key, x -> new AtomicInteger(0));
        counter.incrementAndGet();
    }

    public void postConstruct(@Observes @Initialized(ApplicationScoped.class) Object o) {
        if (fileHelper.exists(path)) {
            fileHelper.lines(path)
                    .forEach(line -> {
                        int index = line.lastIndexOf(" ");
                        map.put(line.substring(0, index), new AtomicInteger(Integer.valueOf(line.substring(index + 1))));
                    });
        }
    }

    public void postDestroy(@Observes @Destroyed(ApplicationScoped.class) Object o) {
        List<String> lines = new ArrayList<>();
        map.forEach((x, y) -> lines.add(x + " " + y.get()));

        fileHelper.write(path, lines);
    }

    protected int getCountForKey(String key) {
        return map.getOrDefault(key, new AtomicInteger(0)).intValue();
    }
}
