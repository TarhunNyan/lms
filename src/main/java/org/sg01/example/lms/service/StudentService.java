package org.sg01.example.lms.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.mapper.StudentMapper;
import org.sg01.example.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentService {

    final StudentRepository studentRepository;
    final StudentMapper studentMapper;

    public StudentDTO getById(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(String.format("Student id=%s not found", id)));
        return studentMapper.toDto(student);
    }

}
