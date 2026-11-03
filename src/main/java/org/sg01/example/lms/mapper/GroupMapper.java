package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    @Mapping(target = "groupId", source = "id")
    @Mapping(target = "studentIdCollection", source = "studentCollection")
    GroupDTO toDTO(Group entity);

    default Long map(Student student) {
        return student.getId();
    }

}
