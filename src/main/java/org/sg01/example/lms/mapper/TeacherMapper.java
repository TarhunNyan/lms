package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.sg01.example.lms.dto.TeacherDTO;
import org.sg01.example.lms.entity.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    TeacherDTO toDTO(Teacher entity);

}
