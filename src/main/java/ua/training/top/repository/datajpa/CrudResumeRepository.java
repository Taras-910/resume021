package ua.training.top.repository.datajpa;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ua.training.top.model.Resume;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudResumeRepository extends JpaRepository<Resume, Integer> {

    @Transactional
    @Modifying
    @Query("DELETE FROM Resume r WHERE r.id=:id")
    int delete(@Param("id") int id);

    @Query("SELECT r FROM Resume r WHERE r.id=:id")
    Resume get(@Param("id") int id);

    @Query("SELECT r FROM Resume r ORDER BY r.releaseDate DESC, r.title ASC ")
    List<Resume> getAll();

    @Query("SELECT r FROM Resume r WHERE r.title=:title AND r.name=:name " +
            "AND LOWER(r.workBefore) LIKE CONCAT(:workBefore, '%')")
    Resume getByParams(@Param("title") String title, @Param("name") String name, @Param("workBefore") String workBefore);

    @Query("SELECT r FROM Resume r WHERE " +
            "(LOWER(r.title) LIKE CONCAT('%',:language,'%') " +
            "OR LOWER(r.skills) LIKE CONCAT('%',:language,'%') " +
            "OR LOWER(r.workBefore) LIKE CONCAT('%',:language,'%')) " +
            "AND (LOWER(r.title) LIKE CONCAT('%',:level,'%')" +
            "OR LOWER(r.skills) LIKE CONCAT('%',:level,'%') " +
            "OR LOWER(r.workBefore) LIKE CONCAT('%',:level,'%'))" +
            "AND LOWER(r.address) LIKE CONCAT('%',:workplace,'%')")
    List<Resume> getByFilter(@Param("language") String language, @Param("level") String level, @Param("workplace") String workplace);

    @Query("SELECT r FROM Resume r WHERE r.freshen.userId=:userId")
    List<Resume> getByUserId(@Param("userId") Integer userId);

    @Query("SELECT r FROM Resume r WHERE r.releaseDate<:reasonPeriodToKeep")
    List<Resume> getOutDated(@Param("reasonPeriodToKeep") LocalDate reasonPeriodToKeep);

    //    https://stackoverflow.com/questions/9314078/setmaxresults-for-spring-data-jpa-annotation
    @Query(value = "SELECT * FROM Resume r ORDER BY r.release_date ASC LIMIT :limit", nativeQuery = true)
    List<Resume> getList(@Param("limit") int limit);

    @Query(value = "SELECT r FROM Resume r ORDER BY r.releaseDate DESC, r.id DESC")
    Page<Resume> getFirstPage(PageRequest pageable);
}

