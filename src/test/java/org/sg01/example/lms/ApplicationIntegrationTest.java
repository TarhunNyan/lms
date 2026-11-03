package org.sg01.example.lms;

import org.junit.jupiter.api.Test;
import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.service.GroupService;
import org.sg01.example.lms.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
class ApplicationIntegrationTest {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    private final GroupService groupService;
    private final StudentService studentService;

    @Autowired
    public ApplicationIntegrationTest(GroupService groupService, StudentService studentService) {
        this.groupService = groupService;
        this.studentService = studentService;
    }

    @Test
    void getStudents_returnsPage() {
        // GIVEN
        String groupName = "БМЕ21-01";
        String studentFirstName = "Анто";
        String studentLastName = "Антонович";

        GroupCreateDTO groupCreateDTO = new GroupCreateDTO(groupName, null);
        Long groupId = groupService.create(groupCreateDTO).getGroupId();
        StudentCreateDTO studentCreateDTO = new StudentCreateDTO(studentFirstName, studentLastName, groupId);
        studentService.create(studentCreateDTO);

        // WHEN
        GroupDTO groupDTO = groupService.getById(groupId);
        List<StudentDTO> list = groupDTO
                .getStudentIdCollection()
                .stream()
                .map(studentService::getById)
                .toList();

        // THEN
        assertNotNull(groupDTO);
        assertEquals(groupName, groupDTO.getGroupName());
        assertFalse(list.isEmpty());
        assertEquals(list.size(), 1);
        assertNotNull(list.getFirst());
        assertEquals(list.getFirst().getFirstname(), studentFirstName);
        assertEquals(list.getFirst().getLastname(), studentLastName);
    }
}