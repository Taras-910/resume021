package ua.training.top.aggregator.installation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.Strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.training.top.aggregator.dispatcher.Dispatcher.allProviders;

public class InstallationUtil {
    private static final Logger log = LoggerFactory.getLogger(InstallationUtil.class);
    public static final int
            limitResumesKeeping = 3000,
            maxAge = 22,
            limitText = 300,
            limitPages = 50;

    public static int repeatOfCalls = 3;
    public static LocalDate
            reasonDateLoading = LocalDateTime.now().toLocalDate().minusDays(90),
            reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(120);

//    public static boolean testProvider = true;
    public static boolean testProvider = false;

    public static void setTestProvider() { InstallationUtil.testProvider = true; }
    public static void offTestProvider() {
        InstallationUtil.testProvider = false;
    }

    public static boolean autoRefreshProviders = false;
    public static void setAutoRefreshProviders() { InstallationUtil.autoRefreshProviders = true; }
    public static void offAutoRefreshProviders() { InstallationUtil.autoRefreshProviders = false; }

    public static void setTestReasonPeriodToKeep() {
        InstallationUtil.reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(365);
    }

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatOfCalls > 0){
            log.info("reCall attemptToCall={}", repeatOfCalls);
            allProviders.addLast(new Provider(strategy));
            repeatOfCalls--;
        }
    }
}
