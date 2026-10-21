package org.sg01.example.lms.service;

import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.dto.TeacherCourseCreateDTO;
import org.sg01.example.lms.dto.TeacherCourseDeleteDTO;

public interface CourseTeacherService {

    CourseDTO addRelation(TeacherCourseCreateDTO requestDTO);

    void removeRelation(TeacherCourseDeleteDTO requestDTO);
}
