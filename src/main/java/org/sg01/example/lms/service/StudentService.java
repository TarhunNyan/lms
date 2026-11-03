package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {

    StudentDTO create(StudentCreateDTO requestDTO);

    StudentDTO getById(Long id);

    Page<StudentDTO> getAll(Pageable pageable);

    StudentDTO update(Long id, StudentUpdateDTO requestDTO);

    void delete(Long id);
}
