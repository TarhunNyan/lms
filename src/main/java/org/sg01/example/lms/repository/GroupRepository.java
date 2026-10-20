package org.sg01.example.lms.repository;

import org.sg01.example.lms.dto.GroupWithStudentIdDTO;
import org.sg01.example.lms.entity.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GroupRepository extends JpaRepository<Group, Long> {

    // избегаем N+1, потому что нужны только id студенов, а findAll по умлчанию возвращает прокси, который ходит в БД при запросе id
    // понятия не имею насколько это павильно... можно навернуться если декартово произведение будет огромным
    @Query("SELECT g.id AS groupId, g.groupName AS groupName, s.id AS studentId FROM Group g LEFT JOIN g.studentList s")
    List<GroupWithStudentIdDTO> findAllWithStudentIds();

}
