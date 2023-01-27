package ua.training.top.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import ua.training.top.AuthorizedUser;
import ua.training.top.Profiles;
import ua.training.top.model.Role;
import ua.training.top.model.User;
import ua.training.top.repository.UserRepository;
import ua.training.top.util.exception.MethodNotAllowedException;

import javax.validation.constraints.NotEmpty;
import java.util.List;

import static ua.training.top.model.AbstractBaseEntity.START_SEQ;
import static ua.training.top.util.InformUtil.must_not_null;
import static ua.training.top.util.UserUtil.prepareToSave;
import static ua.training.top.util.ValidationUtil.*;

@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Service("userService")
public class UserService implements UserDetailsService {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    private boolean modificationRestriction;

    @Autowired
    @SuppressWarnings("deprecation")
    public void setEnvironment(Environment environment) {
        modificationRestriction = environment.acceptsProfiles(Profiles.HEROKU);
    }

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(@NotEmpty User user) throws MethodNotAllowedException{
        log.info("create {}", user);
        Assert.notNull(user, must_not_null);
        checkNew(user);
        return prepareAndSave(user, null);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(int id) {
        checkModificationAllowed(id);
        log.info("delete {}", id);
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(int id) {
        log.info("get {}", id);
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) {
        log.info("getByEmail {}", email);
        Assert.notNull(email, must_not_null);
        return checkNotFound(repository.getByEmail(email), "mail " + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
            log.info("getAll");
            return repository.getAll();
        }

    @CacheEvict(value = "users", allEntries = true)
    public void update(User user, int id) throws MethodNotAllowedException{
        log.info("update {} with id={}", user, id);
        checkModificationAllowed(id);
        assureIdConsistent(user, id);
        Assert.notNull(user, must_not_null);
        checkValidEmail(user.getEmail());
        User userDb = user.getId() == null ? null : repository.get(user.getId());
        checkNotFoundWithId(prepareAndSave(user, userDb), user.id());
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(int id, boolean enabled) {
        log.info(enabled ? "enable {}" : "disable {}", id);
        checkModificationAllowed(id);
        User user = get(id);
        user.setEnabled(enabled);
        repository.save(user);  // !! need only for JDBC implementation
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User with email " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user, User userDb) {
        return repository.save(prepareToSave(user, passwordEncoder, userDb));
    }

    protected void checkModificationAllowed(int id) throws MethodNotAllowedException{
        if (modificationRestriction && id < START_SEQ + 2) {
            String person = id == START_SEQ ? "Admin" : "User";
            throw new MethodNotAllowedException("Изменять учетные данные пользователя " + person + " запрещено!");
        }
    }
    public User asAdmin() {
        log.info("findAdmin");
        return  repository.getAll().stream()
                .filter(u -> u.getRoles().contains(Role.ADMIN))
                .findFirst().get();
    }
}

