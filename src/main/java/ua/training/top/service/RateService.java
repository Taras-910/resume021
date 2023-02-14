package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.AbstractBaseEntity;
import ua.training.top.model.Rate;
import ua.training.top.repository.RateRepository;

import java.io.Serializable;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.training.top.aggregator.Installation.baseCurrency;
import static ua.training.top.aggregator.Installation.reasonValidRate;
import static ua.training.top.aggregator.Provider.getRates;
import static ua.training.top.util.ValidationUtil.checkNew;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RateService extends AbstractBaseEntity implements Serializable {
    private final static Logger log = LoggerFactory.getLogger(RateService.class);
    public final static Map<String, Rate> mapRates = new HashMap<>();

    private final RateRepository repository;

    public RateService(RateRepository repository) {
        this.repository = repository;
    }

    public Rate get(int id) {
        log.info("get by id {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public Rate getByName(String name) {
        log.info("get by name {}", name);
        System.out.println("repository.equals(null)="+(repository == null));
        return repository.getByName(name);
    }

    public List<Rate> getAll() {
        log.info("getAll");
        return repository.getAll();
    }

    @Transactional
    public Rate create(Rate rate) {
        log.info("create {}", rate);
//        Assert.notNull(rate, not_be_null);
        checkNew(rate);
        return repository.save(rate);
    }

    @Transactional
    public void delete(int id) {
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    @Transactional
    public void deleteAll() {
        log.info("deleteAll");
        repository.deleteAll();
    }

    @Transactional
    public void updateRateDB() {
        log.info("update rate by baseCurrency {}", baseCurrency);
        List<Rate> ratesNew = getRates();
        List<Rate> ratesDB = getAll();
        if(!ratesDB.isEmpty()){
            ratesNew.forEach(ratesDB::remove);
            ratesNew.addAll(ratesDB);
        }
        if(!ratesNew.isEmpty() && repository != null){
            repository.deleteAll();
        }
        if(repository != null) repository.saveAll(ratesNew);
    }

    public boolean CurrencyRatesMapInit() {
        log.info("currency rates map init \n{}", ": <|> ".repeat(20));
        getAll().forEach(r -> mapRates.put(r.getName(), r));
        Rate rate = mapRates.values().stream().min(Comparator.comparing(Rate::getDateRate)).orElse(null);
        mapRates.entrySet().forEach(k -> System.out.println(k + " : " + mapRates.get(k)));
        return rate == null || rate.getDateRate().isBefore(reasonValidRate);
    }

}
