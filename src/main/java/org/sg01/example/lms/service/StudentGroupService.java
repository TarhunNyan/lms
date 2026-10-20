package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentGroupCreateDTO;
import org.sg01.example.lms.dto.StudentGroupDeleteDTO;

public interface StudentGroupService {

    StudentDTO addStudentToGroup(StudentGroupCreateDTO requestDTO);

    void removeStudentToGroup(StudentGroupDeleteDTO id);
}
