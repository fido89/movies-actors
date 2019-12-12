package movies.beans;

import movies.helpers.FileHelper;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.nio.file.Files;
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
        if (Files.exists(path)) {
            fileHelper.lines(path)
                    .map(l -> l.split(" "))
                    .forEach(a -> map.put(a[0], new AtomicInteger(Integer.valueOf(a[1]))));
        }
    }

    public void postDestroy(@Observes @Destroyed(ApplicationScoped.class) Object o) {
        List<String> lines = new ArrayList<>();
        map.forEach((x, y) -> lines.add(x + " " + y.get()));

        fileHelper.write(path, lines);
    }
}
