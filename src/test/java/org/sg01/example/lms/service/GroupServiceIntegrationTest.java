package org.sg01.example.lms.service;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.sg01.example.lms.config.HelperTestConfig;
import org.sg01.example.lms.dto.*;
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
import static java.util.function.Predicate.not;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Testcontainers
@Transactional
@Import(HelperTestConfig.class)
class GroupServiceIntegrationTest {

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

    @BeforeEach
    void beforeEach() {
        fooGroupTestHelper.reset();
    }

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
    void create_shouldDeduplicateStudentIds() {
        // GIVEN
        StudentDTO s1 = saveStudent("A");
        StudentDTO s2 = saveStudent("B");

        // WHEN
        GroupCreateDTO groupCreateDTO = new GroupCreateDTO("G1", List.of(s1.getId(), s2.getId(), s1.getId()));
        GroupDTO g1 = groupService.create(groupCreateDTO);

        // THEN
        assertEquals(2, g1
                .getStudentIdCollection()
                .size());
        assertTrue(g1
                .getStudentIdCollection()
                .containsAll(Set.of(s1.getId(), s2.getId())));
    }

    @Test
    void create_shouldDeduplicateStudentIds_atInsert2() {
        // GIVEN
        String ORIG_GROUP_NAME = "G1";
        String NEW_GROUP_NAME = "new group name";
        GroupCreateDTO groupCreateDTO = new GroupCreateDTO(ORIG_GROUP_NAME, null);
        GroupDTO group = groupService.create(groupCreateDTO);

        StudentCreateDTO studentCreateDTO1 = new StudentCreateDTO("Antonio", "Margaretti", group.getGroupId());
        StudentDTO s1 = studentService.create(studentCreateDTO1);
        StudentCreateDTO studentCreateDTO2 = new StudentCreateDTO("Not Antonio", "Not Margaretti", group.getGroupId());
        StudentDTO s2 = studentService.create(studentCreateDTO2);

        // WHEN
        GroupUpdateDTO groupUpdateDTO = new GroupUpdateDTO(NEW_GROUP_NAME, null);
        groupService.update(group.getGroupId(), groupUpdateDTO);

        em.flush();
        em.clear();

        Page<GroupDTO> page = groupService.getAll(PageRequest.of(0, 20));
        GroupDTO g1 = findByName(page, ORIG_GROUP_NAME);
        GroupDTO g2 = findByName(page, NEW_GROUP_NAME);

        // THEN
        assertEquals(1, page
                .getContent()
                .stream()
                .toList()
                .size());
        assertTrue(page
                .getContent()
                .stream()
                .map(GroupDTO::getGroupName)
                .toList()
                .contains(NEW_GROUP_NAME));

        assertEquals(2, g2
                .getStudentIdCollection()
                .size());
        assertTrue(g2
                .getStudentIdCollection()
                .containsAll(Set.of(s1.getId(), s2.getId())));

        assertNull(g1);
    }

    @Test
    void create_shouldNotUpdateStudentIds() {
        // GIVEN
        GroupCreateDTO groupCreateDTO = new GroupCreateDTO("G1", null);
        GroupDTO g1 = groupService.create(groupCreateDTO);

        StudentCreateDTO studentCreateDTO1 = new StudentCreateDTO("Antonio", "Margaretti", g1.getGroupId());
        StudentDTO s1 = studentService.create(studentCreateDTO1);
        StudentCreateDTO studentCreateDTO2 = new StudentCreateDTO("Not Antonio", "Not Margaretti", g1.getGroupId());
        StudentDTO s2 = studentService.create(studentCreateDTO2);

        // WHEN
        GroupUpdateDTO groupUpdateDTO = new GroupUpdateDTO(null, Set.of(s2.getId(), s1.getId()));
        GroupDTO updated = groupService.update(g1.getGroupId(), groupUpdateDTO);

        em.flush();
        em.clear();

        // THEN
        assertEquals(2, updated
                .getStudentIdCollection()
                .size());
        assertTrue(updated
                .getStudentIdCollection()
                .containsAll(Set.of(s1.getId(), s2.getId())));
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
        GroupDTO g1 = findByName(page, GROUP_NAME_1);
        GroupDTO g2 = findByName(page, GROUP_NAME_2);

        // THEN
        assertEquals(2, page
                .getContent()
                .stream()
                .filter(not(fooGroupTestHelper::isFooGroup))
                .toList()
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

    private GroupDTO findByName(Page<GroupDTO> page, String name) {
        List<GroupDTO> pageList = page
                .getContent()
                .stream()
                .toList();
        return findByName(pageList, name);
    }

    private GroupDTO findByName(List<GroupDTO> list, String name) {
        return list
                .stream()
                .filter(g -> g
                        .getGroupName()
                        .equals(name))
                .findFirst()
                .orElse(null);
    }

    private StudentDTO saveStudent(String name) {
        StudentCreateDTO dto = new StudentCreateDTO(name, name, fooGroupTestHelper.getFooGroupId());
        return studentService.create(dto);
    }

}