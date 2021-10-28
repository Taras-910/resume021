package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class DataJpaFreshenRepository implements FreshenRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Sort RECORDED_DATE = Sort.by(Sort.Direction.ASC, "recordedDate");
    private final CrudFreshenRepository crudRepository;

    public DataJpaFreshenRepository(CrudFreshenRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Transactional
    @Override
    public Freshen save(Freshen freshen) {
        return crudRepository.save(freshen);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public Freshen get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public List<Freshen> getAll() {
        return crudRepository.findAll(RECORDED_DATE);
    }

    @Override
    public List<Freshen> getBetween(LocalDateTime finish, LocalDateTime start) {
        List<Freshen> freshens = new ArrayList<>();
        try {
            freshens.addAll(crudRepository.getBetween(finish, start));
        } catch (Exception e) {}
        return freshens;
    }

    @Transactional
    @Override
    public void deleteList(List<Freshen> listToDelete) {
        crudRepository.deleteAll(listToDelete);
    }
}

