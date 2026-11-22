package org.sg01.example.lms.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentUpdateDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.exception.ResourceNotFoundException;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.StudentMapper;
import org.sg01.example.lms.repository.GroupRepository;
import org.sg01.example.lms.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock
    GroupRepository groupRepository;
    @Mock
    StudentRepository studentRepository;
    @Mock
    ExceptionHelperService exception;
    @Mock
    StudentMapper studentMapper;

    @InjectMocks
    StudentServiceImpl studentService;

    @Test
    void create_shouldSaveStudentAndReturnDto() {
        // GIVEN
        Long groupId = 1L;
        String firstname = "Ivan";
        String lastname = "Ivanov";

        Group group = new Group();
        group.setId(groupId);

        StudentDTO expectedDto = new StudentDTO(1L, firstname, lastname, List.of(groupId));

        when(groupRepository.findById(groupId)).thenReturn(Optional.of(group));
        when(studentRepository.save(any(Student.class))).thenAnswer(inv -> inv.getArgument(0));
        when(studentMapper.toDTO(any(Student.class))).thenReturn(expectedDto);

        // WHEN
        StudentCreateDTO createDTO = new StudentCreateDTO(firstname, lastname, groupId);
        StudentDTO result = studentService.create(createDTO);

        // THEN
        assertEquals(expectedDto, result);
        verify(groupRepository).findById(groupId);
        verify(studentRepository).save(any(Student.class));
        verify(studentMapper).toDTO(any(Student.class));
    }

    @Test
    void create_whenGroupMissing_shouldThrow() {
        // GIVEN
        Long groupId = 1L;
        String firstname = "Ivan";
        String lastname = "Ivanov";

        when(groupRepository.findById(groupId)).thenReturn(Optional.empty());
        when(exception.createExceptionNotFoundGroupById(anyLong())).thenReturn(ResourceNotFoundException.of());

        // WHEN
        StudentCreateDTO createDTO = new StudentCreateDTO(firstname, lastname, groupId);
        assertThrows(ResourceNotFoundException.class, () -> studentService.create(createDTO));

        // THEN
        verify(exception).createExceptionNotFoundGroupById(groupId);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void getById_shouldReturnDTO() {
        // GIVEN
        Long studentId = 1L;
        String firstname = "Ivan";
        String lastname = "Ivanov";

        Student student = new Student();
        StudentDTO studentDTO = new StudentDTO(studentId, firstname, lastname, List.of(1L));

        when(studentRepository.findById(studentId)).thenReturn(Optional.of(student));
        when(studentMapper.toDTO(student)).thenReturn(studentDTO);

        // WHEN
        StudentDTO result = studentService.getById(studentId);

        // THEN
        assertEquals(studentDTO, result);
        verify(studentRepository).findById(studentId);
        verify(studentMapper).toDTO(student);
    }

    @Test
    void getById_whenStudentMissing_shouldThrow() {
        // GIVEN
        Long studentId = 1L;

        when(studentRepository.findById(studentId)).thenReturn(Optional.empty());
        when(exception.createExceptionNotFoundStudentById(anyLong())).thenReturn(ResourceNotFoundException.of());

        // WHEN
        assertThrows(ResourceNotFoundException.class, () -> studentService.getById(studentId));

        // THEN
        verify(exception).createExceptionNotFoundStudentById(studentId);
    }

    @Test
    void getAll_shouldMapPage() {
        // GIVEN
        Student student = new Student();
        StudentDTO dto = new StudentDTO(1L, "", "", List.of(1L));
        Page<Student> page = new PageImpl<>(List.of(student));
        Pageable pageable = PageRequest.of(0, 10);

        when(studentRepository.findAll(pageable)).thenReturn(page);
        when(studentMapper.toDTO(student)).thenReturn(dto);

        // WHEN
        Page<StudentDTO> result = studentService.getAll(pageable);

        // THEN
        assertTrue(result.getContent().contains(dto));
        verify(studentRepository).findAll(pageable);
    }

}