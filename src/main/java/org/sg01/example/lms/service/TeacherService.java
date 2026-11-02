package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherService {

    TeacherDTO create(TeacherCreateDTO requestDTO);

    TeacherDTO getById(Long id);

    Page<TeacherDTO> getAll(Pageable pageable);

    TeacherDTO update(Long id, TeacherUpdateDTO requestDTO);

    void delete(Long id);
}
