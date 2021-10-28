package ua.training.top.repository.datajpa;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.repository.UserRepository;

import java.util.List;
import java.util.Optional;

@Transactional(readOnly = true)
@Repository
public class DataJpaUserRepository implements UserRepository {
    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");
    private final CrudUserRepository crudRepository;

    public DataJpaUserRepository(CrudUserRepository crudRepository) {
        this.crudRepository = crudRepository;
    }

    @Transactional
    @Override
    public User save(User user) {
        return crudRepository.save(user);
    }

    @Transactional
    @Override
    public boolean delete(int id) {
        return crudRepository.delete(id) != 0;
    }

    @Override
    public User get(int id) {
        return crudRepository.findById(id).orElse(null);
    }

    @Override
    public User getByEmail(String email) {
        return Optional.ofNullable(crudRepository.getByEmail(email)).orElse(null);
    }

    @Override
    public List<User> getAll() {
        return crudRepository.findAll(SORT_NAME_EMAIL);
    }
}
