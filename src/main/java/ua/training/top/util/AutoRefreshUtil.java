package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.*;
import ua.training.top.model.Freshen;

import java.util.Map;
import java.util.Random;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.UserUtil.asAdmin;

public class AutoRefreshUtil {
    private final static Logger log = LoggerFactory.getLogger(AutoRefreshUtil.class);
    public static final Random random = new Random();

    public static void setRandomDelay(int bound) {
        try {
            int delay = random.nextInt(bound);
            log.info("\n------------ delay={} min {} sec ------------", delay/(1000 * 60), delay%(1000 * 60) / 1000);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static int getKey(int bound) {
        return random.nextInt(bound);
    }

    public static final Map<Integer, Provider> mapStrategies =
            Map.ofEntries(
                    Map.entry(0, new Provider(new DjinniStrategy())),
                    Map.entry(1, new Provider(new RabotaStrategy())),
                    /*Map.entry(3, new Provider(new LinkedinStrategy())),*/
                    Map.entry(2, new Provider(new WorkStrategy())),
                    Map.entry(3, new Provider(new GrcStrategy())),
                    Map.entry(4, new Provider(new HabrStrategy()))
            );

    public static final Map<Integer, String> mapWorkplace =
            Map.of(
                    0, "all",
                    1, "украина",
                    2, "foreign",
                    4, "киев",
                    5, "одесса",
                    6, "харьков",
                    3, "remote",
                    7, "минск",
                    8, "санкт-петербург",
                    9, "москва"
            );

    public static final Map<Integer, String> mapLevel =
            Map.of(
                    0, "all",
                    1, "middle",
                    2, "trainee",
                    3, "junior",
                    4, "senior",
                    5, "expert"
                    );

    public static Freshen randomFreshen(String workplace, String level) {
        return new Freshen(null, now(), "java", level, workplace, singleton(UPGRADE), asAdmin().id());
    }
}
