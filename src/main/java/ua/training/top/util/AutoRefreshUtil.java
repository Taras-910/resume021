package ua.training.top.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.DjinniStrategy;
import ua.training.top.aggregator.strategy.WorkStrategy;
import ua.training.top.model.Freshen;

import java.util.Map;
import java.util.Random;

import static java.time.LocalDateTime.now;
import static java.util.Collections.singleton;
import static ua.training.top.model.Goal.UPGRADE;
import static ua.training.top.util.InformUtil.setting_delay;
import static ua.training.top.util.UserUtil.asAdmin;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.*;

public class AutoRefreshUtil {
    private final static Logger log = LoggerFactory.getLogger(AutoRefreshUtil.class);
    public static final Random random = new Random();

    public static void setRandomDelay(int bound) {
        try {
            int delay = random.nextInt(bound);
            log.info(setting_delay, delay/(1000 * 60), delay%(1000 * 60) / 1000);
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
                    Map.entry(1, new Provider(new WorkStrategy()))
            );

    public static final Map<Integer, String> mapWorkplace =
            Map.of(
                    0, "all",
                    1, "украина",
                    2, "foreign",
                    3, "киев",
                    4, "remote",
                    5, "toronto",
                    6, "варшава",
                    7, "львов"
            );

    public static final Map<Integer, String> mapLevel =
            Map.of(
                    0, middle,
                    1, all,
                    2, senior,
                    3, expert,
                    4, junior,
                    5, trainee
                    );

    public static Freshen randomFreshen(String workplace, String level) {
        return new Freshen(null, now(), "java", level, workplace, singleton(UPGRADE), asAdmin().id());
    }
}
