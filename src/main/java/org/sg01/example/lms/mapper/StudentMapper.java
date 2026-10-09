package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    StudentDTO toDto(Student student);

    Student toEntity(StudentDTO dto);

}
