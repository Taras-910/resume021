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
import static ua.training.top.aggregator.installation.Installation.offAutoRefreshProviders;
import static ua.training.top.aggregator.installation.Installation.setAutoRefreshProviders;
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

    //    @Scheduled(cron = "0 0,5,10,15,20,25,30,35,40,45,50,55 6-23 * * *")
    @Scheduled(cron = "0 0,20,40 10-18 * * MON-FRI")
    public void weekDay() {
//        int delayWithinMinutes = 4;
        int delayWithinMinutes = 19;
        log.info(delay, delayWithinMinutes);
        setRandomDelay(1000 * 60 * delayWithinMinutes);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(new Freshen(randomFreshen(mapWorkplace.get(getKey(8) + 2), mapLevel.get(3))));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 0,30 11-17 * * SAT")
    public void weekEnd() {
        int delayMinutesMax = 29;
        log.info(delay, delayMinutesMax);
        setRandomDelay(1000 * 60 * delayMinutesMax);
        setTestAuthorizedUser(asAdmin());
        setAutoRefreshProviders();
        aggregatorService.refreshDB(new Freshen(randomFreshen(mapWorkplace.get(getKey(3)), mapLevel.get(2))));
        offAutoRefreshProviders();
    }

    @Scheduled(cron = "0 45 9,15 * * *")
    public void everyDay() {
        log.info("Scheduled everyDay");
        setTestAuthorizedUser(asAdmin());
        aggregatorService.deleteOutDated();
    }
}
//	      djinni   grc*10   habr  rabota   work  linkedin  total
//all	    49	  49(0)	     1	     6	    16	   (100)	121
//Украина	32	   4(0)	     -	     6	    30	     -	     72
//foreign	49	   2(0)	     1	     1	     -	     -	     53
//Киев	    15	   1(0)	     1	     3	    15	     -	     35
//remote 	 -	  17(0)	     1	     3	    12	    (5)	     33
//Минск	     1	  10(0)	     1	     6	     -	     -	     18
//Львов	     6	    -	     -	     8	     2	     -	     16
//Харьков	 5	    -	     -	     2	     5	     -	     12
//Одесса	 5	    -	     -	     2	     4	     -	     11
//Санкт-П	 5	   5(0)	     1	     -	     -	     -	     11
//Москва	 -	   8(0)	     1	     -	     -	     -	      9

