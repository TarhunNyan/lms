package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    @Mapping(target = "groupIdList", source = "groupList")
    StudentDTO toDTO(Student entity);

    default Long map(Group group) {
        System.out.println("HERHRHRHRH!");
        return group.getId();
    }

}
