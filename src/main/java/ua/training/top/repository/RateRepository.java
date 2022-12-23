package ua.training.top.repository;

import ua.training.top.model.Rate;

import java.util.List;

public interface RateRepository {
    // null if not found
    Rate get(int id);

    List<Rate> getAll();

    // null if not found, when updated
    Rate save(Rate rate);

    List<Rate> saveAll(List<Rate> rates);

    // false if not found
    boolean delete(int id);

    void deleteAll();

    Rate getByName(String name);
}
