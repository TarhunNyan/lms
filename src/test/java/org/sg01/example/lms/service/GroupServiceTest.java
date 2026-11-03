package org.sg01.example.lms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.dto.GroupUpdateDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.exception.GroupHasStudentsException;
import org.sg01.example.lms.exception.ResourceNotFoundException;
import org.sg01.example.lms.exception.StudentNotInGroupException;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.GroupMapper;
import org.sg01.example.lms.repository.GroupRepository;
import org.sg01.example.lms.repository.StudentRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {

    @Mock
    GroupRepository groupRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    ExceptionHelperService exception;
    @Mock
    GroupMapper groupMapper;

    @InjectMocks
    GroupServiceImpl groupService;

    @ParameterizedTest
    @MethodSource("provideEmptyStudentIdCollection")
    void create_whenStudentListIsNull_shouldSaveGroupWithoutStudents(List<Long> studentIdCollection) {
        // GIVEN
        Group group = new Group();
        when(groupRepository.save(any())).thenReturn(group);
        GroupDTO groupDTO = new GroupDTO(1L, "", List.of());
        when(groupMapper.toDTO(any())).thenReturn(groupDTO);
        GroupCreateDTO dtoWithNull = new GroupCreateDTO("G-1", studentIdCollection);

        // WHEN
        groupService.create(dtoWithNull);

        // THEN
        verify(studentRepository, never()).existsAllByIdCollection(any());
        verify(groupRepository).save(any(Group.class));
    }

    private static Stream<Arguments> provideEmptyStudentIdCollection() {
        return Stream.of(Arguments.of((List<Long>) null), Arguments.of(List.of()), Arguments.of(new ArrayList<>()));
    }

    @Test
    void create_whenSomeStudentsMissing_shouldThrowWithDiff() {
        // GIVEN
        GroupCreateDTO createDTO = new GroupCreateDTO("G-1", List.of(1L, 2L, 3L));
        when(studentRepository.existsAllByIdCollection(anySet())).thenReturn(Set.of(1L));
        when(exception.createExceptionNotFoundStudentListById(anySet())).thenReturn(ResourceNotFoundException.of());

        // WHEN
        assertThrows(ResourceNotFoundException.class, () -> groupService.create(createDTO));

        // THEN
        verify(exception).createExceptionNotFoundStudentListById(Set.of(2L, 3L));
        verify(groupRepository, never()).save(any());
    }

    @Test
    void getById_whenNotFound_shouldThrow() {
        // GIVEN
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());
        when(exception.createExceptionNotFoundGroupById(1L)).thenReturn(ResourceNotFoundException.of());

        // WHEN
        assertThrows(ResourceNotFoundException.class, () -> groupService.getById(1L));

        // THEN
        verify(groupMapper, never()).toDTO(any());
    }

    @Test
    void update_whenStudentMissing_shouldThrowWithDiff() {
        // GIVEN
        Group group = new Group();
        when(studentRepository.findAllById(anySet())).thenReturn(List.of()); // никого
        when(exception.createExceptionNotFoundStudentListById(anySet())).thenReturn(ResourceNotFoundException.of());
        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));

        // WHEN
        GroupUpdateDTO updateDTO = new GroupUpdateDTO("", Set.of(1L, 2L));
        assertThrows(ResourceNotFoundException.class, () -> groupService.update(1L, updateDTO));

        // THEN
        verify(exception).createExceptionNotFoundStudentListById(Set.of(1L, 2L));
    }

    @Test
    void update_whenStudentWouldBeLeftWithoutGroup_shouldThrow() {
        // GIVEN
        Student student = new Student();
        student.setId(5L);

        Group group = new Group();
        group
                .getStudentCollection()
                .add(student);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentRepository.findAllById(anySet())).thenReturn(List.of());
        when(studentRepository.findStudentIdCollectionWithSingleGroup(anySet())).thenReturn(List.of(5L));

        // WHEN
        GroupUpdateDTO updateDTO = new GroupUpdateDTO("", Set.of());
        assertThrows(StudentNotInGroupException.class, () -> groupService.update(1L, updateDTO));

        // THEN
        assertFalse(group
                .getStudentCollection()
                .isEmpty());
    }

    @Test
    void delete_whenStudentsLeftWithoutGroup_shouldThrow() {
        // GIVEN
        Student student = new Student();
        student.setId(5L);

        Group group = new Group();
        group
                .getStudentCollection()
                .add(student);

        when(groupRepository.findById(1L)).thenReturn(Optional.of(group));
        when(studentRepository.findStudentIdCollectionWithSingleGroup(List.of(5L))).thenReturn(List.of(1L));

        // WHEN
        assertThrows(GroupHasStudentsException.class, () -> groupService.delete(1L));

        // THEN
        verify(groupRepository, never()).delete(any());
    }

    @Test
    void delete_whenNotFound_shouldThrow() {
        // GIVEN
        when(groupRepository.findById(1L)).thenReturn(Optional.empty());
        when(exception.createExceptionNotFoundGroupById(1L)).thenReturn(ResourceNotFoundException.of());

        // WHEN
        assertThrows(RuntimeException.class, () -> groupService.delete(1L));

        // THEN
        verify(groupRepository, never()).delete(any());
    }

}