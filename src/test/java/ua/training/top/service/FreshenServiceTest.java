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
import static ua.training.top.testData.UserTestData.NOT_FOUND;
import static ua.training.top.testData.UserTestData.admin;

public class FreshenServiceTest extends AbstractServiceTest{

    @Autowired
    private FreshenService service;

    @Test
    public void create() throws Exception {
        setTestAuthorizedUser(admin);
        Freshen created = service.create(FreshenTestData.getNew());
        int newId = created.id();
        Freshen newFreshen = FreshenTestData.getNew();
        newFreshen.setId(newId);
        FRESHEN_MATCHER.assertMatch(created, newFreshen);
        FRESHEN_MATCHER.assertMatch(service.get(newId), newFreshen);
    }

    @Test
    @Transactional
    public void delete() throws Exception {
        service.delete(FRESHEN1_ID);
        assertThrows(NotFoundException.class, () -> service.get(FRESHEN1_ID));
    }

    @Test
    public void deletedNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    public void get() throws Exception {
        Freshen freshen = service.get(FRESHEN1_ID);
        FRESHEN_MATCHER.assertMatch(freshen, freshen1);
    }

    @Test
    public void getNotFound() throws Exception {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    public void update() throws Exception {
        Freshen updated = FreshenTestData.getUpdated();
        service.update(updated, FRESHEN1_ID);
        FRESHEN_MATCHER.assertMatch(service.get(FRESHEN1_ID), FreshenTestData.getUpdated());
    }

    @Test
    public void getAll() throws Exception {
        List<Freshen> all = service.getAll();
        FRESHEN_MATCHER.assertMatch(all, freshen1, freshen2);
    }

}
