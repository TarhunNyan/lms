package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.CourseCreateDTO;
import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.dto.CourseUpdateDTO;

import java.util.List;

public interface CourseService {

    CourseDTO create(CourseCreateDTO requestDTO);

    CourseDTO getById(Long id);

    List<CourseDTO> getAll();

    CourseDTO update(Long id, CourseUpdateDTO requestDTO);

    void delete(Long id);
}
