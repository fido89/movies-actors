package movies.beans;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Destroyed;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@ApplicationScoped
public class StatisticBean {

    private Map<String, AtomicInteger> map = new HashMap<>();
    private Path path = Path.of("./data/stats.txt");

    public void putToStatistic(String key) {
        AtomicInteger counter = map.computeIfAbsent(key, x -> new AtomicInteger(0));
        System.out.println(key + " " + counter.incrementAndGet());
    }

    public void postConstruct(@Observes @Initialized(ApplicationScoped.class) Object o) {
        if (Files.exists(path)) {
            try {
                Files.lines(path)
                        .map(l -> l.split(" "))
                        .forEach(a -> map.put(a[0], new AtomicInteger(Integer.valueOf(a[1]))));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void postDestroy(@Observes @Destroyed(ApplicationScoped.class) Object o) {
        List<String> lines = new ArrayList<>();
        map.forEach((x, y) -> lines.add(x + " " + y.get()));
        try {
            Files.write(path, lines);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
