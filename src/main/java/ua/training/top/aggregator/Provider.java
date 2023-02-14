package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.rate.RateProvider;
import ua.training.top.aggregator.rate.TradingEconomicsProvider;
import ua.training.top.aggregator.strategy.Strategy;
import ua.training.top.model.Freshen;
import ua.training.top.model.Rate;
import ua.training.top.to.ResumeTo;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import static ua.training.top.aggregator.Installation.baseCurrency;
import static ua.training.top.util.InformUtil.number_inform;
import static ua.training.top.util.aggregateUtil.data.ConstantsUtil.ratesAria;

public class Provider {
    private static final Logger log = LoggerFactory.getLogger(Provider.class);
    private final Strategy strategy;

    public Provider(Strategy strategy) {
        this.strategy = strategy;
    }

    public List<ResumeTo> getResumes(Freshen freshen) throws IOException {
        List<ResumeTo> list = strategy.getResumes(freshen);
        log.info(number_inform, this.strategy.getClass().getSimpleName(), list.size());
        return list;
    }

    public static List<Rate> getRates(){
        RateProvider rateProvider = new TradingEconomicsProvider();
//        RateProvider rateProvider = new TestProvider();
        return rateProvider.getRates(baseCurrency).stream()
                .filter(r -> ratesAria.contains(r.getName()))
                .collect(Collectors.toList());
    }
}
