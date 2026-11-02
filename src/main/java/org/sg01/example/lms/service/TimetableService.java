package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.*;

import java.awt.print.Pageable;
import java.util.List;

public interface TimetableService {

    // CRUD
    TimetableDTO create(TimetableCreateDTO requestDTO);

    TimetableDTO getById(Long id);

    List<TimetableDTO> getAll();

    TimetableDTO update(Long id, TimetableUpdateDTO requestDTO);

    void delete(Long id);

    // Специфичные GET
//    List<TimetableDTO> findByGroup(Long groupId, Pageable pageable);

}
