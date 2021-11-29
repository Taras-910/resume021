package ua.training.top.repository.datajpa;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.repository.FreshenRepository;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
@Repository
public class DataJpaFreshenRepository implements FreshenRepository {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    private static final Sort RECORDED_DATE = Sort.by(Sort.Direction.ASC, "recordedDate");
    private final CrudFreshenRepository freshenRepository;

    public DataJpaFreshenRepository(CrudFreshenRepository crudRepository) {
        this.freshenRepository = crudRepository;
    }

    @Transactional
    @Override
    public Freshen save(Freshen freshen) {
        return freshenRepository.save(freshen);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return freshenRepository.delete(id) != 0;
    }

    @Override
    public Freshen get(int id) {
        return freshenRepository.findById(id).orElse(null);
    }

    @Override
    public List<Freshen> getAll() {
        return freshenRepository.findAll(RECORDED_DATE);
    }

    @Transactional
    @Override
    public void deleteList(List<Freshen> listToDelete) {
        freshenRepository.deleteAll(listToDelete);
    }

    @Transactional
    @Override
    public void deleteOutDated(LocalDateTime reasonLocalDateTime) {
        deleteList(freshenRepository.getOutDated(reasonLocalDateTime));
    }
}

