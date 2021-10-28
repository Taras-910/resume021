package ua.training.top.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Freshen;

import java.time.LocalDateTime;
import java.util.List;

public interface CrudFreshenRepository extends JpaRepository<Freshen, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Freshen f WHERE f.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT f FROM Freshen f WHERE f.recordedDate<=:finish AND f.recordedDate>=:start")
    List<Freshen> getBetween(@Param("finish") LocalDateTime finish, @Param("start")LocalDateTime start);
}
