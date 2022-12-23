package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Rate;
import ua.training.top.repository.RateRepository;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static ua.training.top.aggregator.Installation.baseCurrency;
import static ua.training.top.aggregator.Provider.getRates;
import static ua.training.top.util.ValidationUtil.checkNew;
import static ua.training.top.util.ValidationUtil.checkNotFoundWithId;

@Service
public class RateService {
    private final static Logger log = LoggerFactory.getLogger(RateService.class);
    public static Map<String, Rate> mapRates = new HashMap<>();

    @Autowired
    private RateRepository repository;

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

    @PostConstruct
    public void CurrencyRatesMapInit() {
        log.info("currency rates map init \n{}\n", ": <|> ".repeat(20));
        List<Rate> rates = getAll();
        rates.forEach(r -> mapRates.put(r.getName(), r));
        System.out.println("\nmapRates=\n"+mapRates.toString());
    }
}
