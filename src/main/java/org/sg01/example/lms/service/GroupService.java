package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.dto.GroupUpdateDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface GroupService {

    GroupDTO create(GroupCreateDTO requestDTO);

    GroupDTO getById(Long id);

    Page<GroupDTO> getAll(Pageable pageable);

    GroupDTO update(Long id, GroupUpdateDTO requestDTO);

    void delete(Long id);
}
