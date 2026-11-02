package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TimetableService {

    // CRUD
    TimetableDTO create(TimetableCreateDTO requestDTO);

    TimetableDTO getById(Long id);

    List<TimetableDTO> getAll();

    Page<TimetableDTO> getAll(Pageable pageable);

    TimetableDTO update(Long id, TimetableUpdateDTO requestDTO);

    void delete(Long id);

    // Специфичные GET
//    List<TimetableDTO> findByGroup(Long groupId, Pageable pageable);

}
