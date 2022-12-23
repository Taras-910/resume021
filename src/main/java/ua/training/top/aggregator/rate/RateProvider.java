package ua.training.top.aggregator.rate;

import ua.training.top.model.Rate;

import java.util.List;

public interface RateProvider {
    List<Rate> getRates (String baseCurrency);
}
