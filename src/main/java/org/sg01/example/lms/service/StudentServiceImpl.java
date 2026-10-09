package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentUpdateDTO;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.exception.ResourceNotFoundException;
import org.sg01.example.lms.mapper.StudentMapper;
import org.sg01.example.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    final StudentRepository studentRepository;
    final StudentMapper studentMapper;

    @Override
    public StudentDTO create(StudentCreateDTO createDTO) {
        Student student = new Student();
        student.setFirstname(createDTO.getFirstname());
        student.setLastname(createDTO.getLastname());
        student.setGroups(null);
        studentRepository.save(student);

        return studentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getById(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> createExceptionNotFoundStudentById(id));
        return studentMapper.toDto(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAll() {
        return studentRepository.findAll().stream().map(studentMapper::toDto).toList();
    }

    @Override
    @Transactional
    public StudentDTO update(Long id, StudentUpdateDTO updateDTO) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> createExceptionNotFoundStudentById(id));

        if (updateDTO.getFirstname() != null) {
            student.setFirstname(updateDTO.getFirstname());
        }
        if (updateDTO.getLastname() != null) {
            student.setLastname(updateDTO.getLastname());
        }

        studentRepository.save(student);
        return studentMapper.toDto(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!studentRepository.existsById(id)) {
            throw createExceptionNotFoundStudentById(id);
        }
        studentRepository.deleteById(id);
    }

    private ResourceNotFoundException createExceptionNotFoundStudentById(Long id) {
        return ResourceNotFoundException.of("student", "id", id);
    }
}
