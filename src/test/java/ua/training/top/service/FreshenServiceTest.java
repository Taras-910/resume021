package ua.training.top.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;
import ua.training.top.testData.FreshenTestData;
import ua.training.top.util.exception.NotFoundException;

import java.util.List;

import static org.junit.Assert.assertThrows;
import static ua.training.top.SecurityUtil.setTestAuthorizedUser;
import static ua.training.top.testData.FreshenTestData.*;
import static ua.training.top.testData.UserTestData.admin;
import static ua.training.top.testData.UserTestData.not_found;

public class FreshenServiceTest extends AbstractServiceTest {

    @Autowired
    private FreshenService service;

    @Test
    public void create() throws Exception {
        setTestAuthorizedUser(admin);
        Freshen created = service.create(FreshenTestData.getNew());
        int newId = created.id();
        Freshen newFreshen = FreshenTestData.getNew();
        newFreshen.setId(newId);
        freshen_matcher.assertMatch(created, newFreshen);
        freshen_matcher.assertMatch(service.get(newId), newFreshen);
    }

    @Test
    @Transactional
    public void delete() throws Exception {
        service.delete(freshen1_id);
        assertThrows(NotFoundException.class, () -> service.get(freshen1_id));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(not_found));
    }

    @Test
    public void get() throws Exception {
        Freshen freshen = service.get(freshen1_id);
        freshen_matcher.assertMatch(freshen, freshen1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(not_found));
    }

    @Test
    public void update() throws Exception {
        Freshen updated = FreshenTestData.getUpdated();
        service.update(updated, freshen1_id);
        freshen_matcher.assertMatch(service.get(freshen1_id), FreshenTestData.getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<Freshen> all = service.getAll();
        freshen_matcher.assertMatch(all, freshen1, freshen2);
    }

}
