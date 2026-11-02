package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentUpdateDTO;

import java.util.List;

public interface StudentService {

    StudentDTO create(StudentCreateDTO requestDTO);

    StudentDTO getById(Long id);

    List<StudentDTO> getAll();

    StudentDTO update(Long id, StudentUpdateDTO requestDTO);

    void delete(Long id);
}
