package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Rate;

import java.util.List;

public interface CrudRateRepository extends JpaRepository<Rate, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Rate e WHERE e.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT e FROM Rate e ORDER BY e.name ASC")
    List<Rate> getAll();

    @Query("SELECT e FROM Rate e WHERE e.name=:name")
    Rate getByName(@Param("name") String name);

    @Transactional
    @Modifying
    @Query("DELETE FROM Rate e")
    void deleteList();
}
