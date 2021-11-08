package ua.training.top.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ua.training.top.aggregator.strategy.Strategy;
import ua.training.top.model.Freshen;
import ua.training.top.to.ResumeTo;

import java.io.IOException;
import java.util.List;

import static ua.training.top.util.InformUtil.number_inform;

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
}
