package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.training.top.model.Freshen;

import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.aggregator.Installation.offAutoRefreshProviders;
import static ua.training.top.aggregator.Installation.setAutoRefreshProviders;
import static ua.training.top.util.AutoRefreshUtil.*;
import static ua.training.top.util.InformUtil.delay;
import static ua.training.top.util.UserUtil.asAdmin;

@Component
@EnableScheduling
@EnableAsync
public class AutoRefreshService {
    public static final Logger log = LoggerFactory.getLogger(AutoRefreshService.class);
    @Autowired
    private AggregatorService aggregatorService;

    @Scheduled(cron = "0 0 10-16 * * MON-FRI")
    public void weekDay() {
        int delayWithinMinutes = 59;
        log.info(delay, delayWithinMinutes);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(new Freshen(randomFreshen(mapWorkplace.get(getKey(8)), mapLevel.get(3))));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 0 11 * * SAT")
    public void weekEnd() {
        int delayMinutesMax = 360;
        log.info(delay, delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(new Freshen(randomFreshen(mapWorkplace.get(getKey(8)), mapLevel.get(2))));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 55 9 * * MON-FRI")
    public void everyDay() {
        log.info("Scheduled everyDay");
        setTestAuthorizedUser(asAdmin());
        aggregatorService.deleteOutDated();
    }

    @Scheduled(cron = "0 0 10 * * MON,THU")
    public void TwiceByWeek() {
        log.info("Scheduled everyDay");
        int delayWithinMinutes = 480; // 8 hours
        log.info("There is set delay within {} minutes", delayWithinMinutes);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        aggregatorService.updateRateDB();
    }
}
