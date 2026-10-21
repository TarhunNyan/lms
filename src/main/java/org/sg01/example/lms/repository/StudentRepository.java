package org.sg01.example.lms.repository;

import org.sg01.example.lms.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface StudentRepository extends JpaRepository<Student, Long> {

    @Query("SELECT u.id FROM Student u WHERE u.id IN :ids")
    Set<Long> existsAllByIdList(@Param("ids") Collection<Long> ids);

    @Query("""
        SELECT count(*) FROM Student s
        JOIN s.groupList g
        WHERE s.id IN :studentIdList
        GROUP BY s.id
        HAVING count(g) = 1
    """)
    Collection<Long> findStudentIdsWithSingleGroup(@Param("studentIdList") Collection<Long> studentIdList);

}
