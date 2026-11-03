package org.sg01.example.lms.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.sg01.example.lms.config.HelperTestConfig;
import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.exception.ResourceNotFoundException;
import org.sg01.example.lms.helper.FooGroupTestHelper;
import org.sg01.example.lms.repository.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@Import(HelperTestConfig.class)
class GroupServiceIT {

    @Container
    @ServiceConnection
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @Autowired
    private GroupService groupService;
    @Autowired
    private StudentService studentService;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private EntityManager em;
    @Autowired
    private FooGroupTestHelper fooGroupTestHelper;

    @Test
    void create_whenStudentDoesNotExist_shouldThrow() {
        // GIVEN
        GroupCreateDTO dto = new GroupCreateDTO("G-1", List.of(999L));

        // WHEN
        // THEN
        assertThrows(ResourceNotFoundException.class, () -> groupService.create(dto));
    }

    @Test
    void create_shouldPersistGroupWithStudents() {
        // GIVEN
        StudentDTO s1 = saveStudent("Ivan");
        StudentDTO s2 = saveStudent("Petr");
        String groupName = "G-1";

        GroupCreateDTO dto = new GroupCreateDTO(groupName, List.of(s1.getId(), s2.getId()));
        GroupDTO result = groupService.create(dto);

        em.flush();
        em.clear();

        // WHEN
        Group saved = groupRepository
                .findById(result.getGroupId())
                .orElseThrow();

        // THEN
        assertEquals(groupName, saved.getGroupName());
        assertArrayEquals(
                dto
                        .getStudentIdCollection()
                        .stream()
                        .sorted()
                        .toArray(),
                saved
                        .getStudentCollection()
                        .stream()
                        .map(Student::getId)
                        .sorted()
                        .toArray()
        );
    }

    @Test
    void getAll_groupWithoutStudents_shouldReturnEmptyList() {
        // GIVEN
        GroupCreateDTO groupCreateDTO = new GroupCreateDTO("EMPTY", List.of());
        groupService.create(groupCreateDTO);

        em.flush();
        em.clear();

        // WHEN
        Page<GroupDTO> page = groupService.getAll(PageRequest.of(0, 20));

        // THEN
        assertEquals(1, page
                .getContent()
                .size());
        assertTrue(page
                .getContent()
                .stream()
                .findFirst()
                .get()
                .getStudentIdCollection()
                .isEmpty());
    }

    @Test
    void getAll_shouldGroupStudentsByGroupCorrectly() {
        // GIVEN
        StudentDTO s1 = saveStudent("A");
        StudentDTO s2 = saveStudent("B");

        String GROUP_NAME_1 = "G1";
        GroupCreateDTO groupCreateDTO1 = new GroupCreateDTO(GROUP_NAME_1, List.of(s1.getId(), s2.getId(), s1.getId()));
        groupService.create(groupCreateDTO1);

        String GROUP_NAME_2 = "G2";
        GroupCreateDTO groupCreateDTO2 = new GroupCreateDTO(GROUP_NAME_2, List.of(s1.getId()));
        groupService.create(groupCreateDTO2);

        em.flush();
        em.clear();

        // WHEN
        Page<GroupDTO> page = groupService.getAll(PageRequest.of(0, 20));
        GroupDTO g1 = findByName(page
                .getContent()
                .stream()
                .toList(), GROUP_NAME_1);
        GroupDTO g2 = findByName(page
                .getContent()
                .stream()
                .toList(), GROUP_NAME_2);

        // THEN
        assertEquals(2, page
                .getContent()
                .size());
        assertTrue(page
                .getContent()
                .stream()
                .map(GroupDTO::getGroupName)
                .toList()
                .containsAll(Set.of(GROUP_NAME_1, GROUP_NAME_2)));

        assertEquals(2, g1
                .getStudentIdCollection()
                .size());
        assertTrue(g1
                .getStudentIdCollection()
                .containsAll(Set.of(s1.getId(), s2.getId())));

        assertEquals(1, g2
                .getStudentIdCollection()
                .size());
        assertTrue(g2
                .getStudentIdCollection()
                .contains(s1.getId()));
    }

    private GroupDTO findByName(List<GroupDTO> list, String name) {
        return list
                .stream()
                .filter(g -> g
                        .getGroupName()
                        .equals(name))
                .findFirst()
                .orElseThrow();
    }

    private StudentDTO saveStudent(String name) {
        StudentCreateDTO dto = new StudentCreateDTO(name, name, fooGroupTestHelper.getFooGroupId());
        return studentService.create(dto);
    }

}