package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Rate;
import ua.training.top.repository.RateRepository;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaRateRepository implements RateRepository {
    private final Logger log = LoggerFactory.getLogger(getClass());
    private final CrudRateRepository repository;

    public DataJpaRateRepository(CrudRateRepository repository) {
        this.repository = repository;
    }

    @Override
    public Rate get(int id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public Rate getByName(String name) {
        return repository.getByName(name);
    }

    @Override
    public List<Rate> getAll() {
        return Optional.of(repository.getAll()).orElse(null);
    }

    @Transactional
    @Override
    public Rate save(Rate rate) {
        return repository.save(rate);
    }

    @Transactional
    @Override
    public List<Rate> saveAll(List<Rate> rates) {
        return repository.saveAll(rates);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return Optional.of(repository.delete(id)).orElse(0) != 0;
    }

    @Transactional
    @Override
    public void deleteAll() {
        repository.deleteList();
    }
}
