package ua.training.top.aggregator.installation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.Provider;
import ua.training.top.aggregator.strategy.Strategy;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static ua.training.top.aggregator.dispatcher.Dispatcher.allProviders;

public class Installation {
    private static final Logger log = LoggerFactory.getLogger(Installation.class);
    public static final float
            rate_pln_to_usd = 3.98f,
            rate_eur_to_usd = 0.86f,
            rate_gbp_to_usd = 0.73f,
            rate_byn_to_usd = 2.43f,
            rate_hrn_to_usd = 26.25f,
            rate_rub_to_usd = 71.78f,
            rate_kzt_to_usd = 426.74f,
            usd_one_to_one = 1.0f;

    public static int
            maxAge = 22,
            maxLengthText = 300,
            limitResumesKeeping = 3000,
            limitPages = 50,
            repeatOfCalls = 3;

    public static LocalDate
            reasonDateLoading = LocalDateTime.now().toLocalDate().minusDays(90),
            reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(120);

//    public static boolean testProvider = true;
    public static boolean testProvider = false;

    public static void setTestProvider() { Installation.testProvider = true; }
    public static void offTestProvider() {
        Installation.testProvider = false;
    }

    public static boolean autoRefreshProviders = false;
    public static void setAutoRefreshProviders() { Installation.autoRefreshProviders = true; }
    public static void offAutoRefreshProviders() { Installation.autoRefreshProviders = false; }

    public static void setTestReasonPeriodToKeep() {
        Installation.reasonPeriodKeeping = LocalDateTime.now().toLocalDate().minusDays(365);
    }

    public static void reCall(int listSize, Strategy strategy){
        if (listSize == 0 && repeatOfCalls > 0){
            log.info("reCall attemptToCall={}", repeatOfCalls);
            allProviders.addLast(new Provider(strategy));
            repeatOfCalls--;
        }
    }
}
