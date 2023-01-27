package ua.training.top.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.AbstractServiceTest;
import ua.training.top.model.Freshen;
import ua.training.top.testData.FreshenTestData;
import ua.training.top.util.exception.IllegalRequestDataException;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.testData.FreshenTestData.*;
import static ua.training.top.testData.UserTestData.not_found;

class FreshenServiceTest extends AbstractServiceTest {

    @Autowired
    private FreshenService service;

    @Test
    void get() {
        Freshen freshen = service.get(freshen1_id);
        freshen_matcher.assertMatch(freshen, freshen1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(not_found));
    }

    @Test
    void getAll() {
        List<Freshen> all = service.getAll();
        freshen_matcher.assertMatch(all, freshen1, freshen2);
    }

    @Test
    void create() {
        Freshen created = service.create(FreshenTestData.getNew());
        int newId = created.id();
        Freshen newFreshen = FreshenTestData.getNew();
        newFreshen.setId(newId);
        freshen_matcher.assertMatch(created, newFreshen);
        freshen_matcher.assertMatch(service.get(newId), newFreshen);
    }

    @Test
    void createInvalid() {
        Freshen invalid = new Freshen(freshen1);
        invalid.setWorkplace("n".repeat(101));
        assertThrows(IllegalRequestDataException.class, () -> service.create(invalid));
    }

    @Test
    void update() {
        Freshen updated = FreshenTestData.getUpdated();
        service.update(updated, freshen1_id);
        freshen_matcher.assertMatch(service.get(freshen1_id), FreshenTestData.getUpdated());
    }

    @Test
    @Transactional
    void delete() {
        service.delete(freshen1_id);
        assertThrows(NotFoundException.class, () -> service.get(freshen1_id));
    }

    @Test
    void deletedNotFound() {
        validateRootCause(NotFoundException.class, () -> service.delete(not_found));
    }
}
