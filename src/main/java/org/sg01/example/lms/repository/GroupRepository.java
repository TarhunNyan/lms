package org.sg01.example.lms.repository;

import org.sg01.example.lms.dto.GroupToStudentIdDTO;
import org.sg01.example.lms.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.Set;

public interface GroupRepository extends JpaRepository<Group, Long> {

    @Query("""
                SELECT g.id AS groupId, s.id AS studentId
                FROM Group g LEFT JOIN g.studentCollection s
                WHERE g.id IN :groupIds
            """)
    Set<GroupToStudentIdDTO> findStudentIdsByGroupIds(@Param("groupIds") Collection<Long> groupIds);

}
