package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.CourseCreateDTO;
import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.dto.CourseUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {

    CourseDTO create(CourseCreateDTO requestDTO);

    CourseDTO getById(Long id);

    List<CourseDTO> getAll();

    Page<CourseDTO> getAll(Pageable pageable);

    CourseDTO update(Long id, CourseUpdateDTO requestDTO);

    void delete(Long id);
}
