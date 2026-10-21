package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.*;

import java.util.List;

public interface TeacherService {

    TeacherDTO create(TeacherCreateDTO requestDTO);

    TeacherDTO getById(Long id);

    List<TeacherDTO> getAll();

    TeacherDTO update(Long id, TeacherUpdateDTO requestDTO);

    void delete(Long id);
}
