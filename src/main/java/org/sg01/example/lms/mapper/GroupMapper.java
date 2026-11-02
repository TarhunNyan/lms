package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.dto.GroupWithStudentIdDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Mapper(componentModel = "spring")
public interface GroupMapper {

    GroupDTO toDTO(GroupWithStudentIdDTO dto);

    @Mapping(target = "groupId", source = "id")
    @Mapping(target = "studentIdCollection", source = "studentCollection")
    GroupDTO toDTO(Group entity);

    default Long map(Student student) {
        return student.getId();
    }

    default List<GroupDTO> map(List<GroupWithStudentIdDTO> rows) {
        Map<Long, GroupDTO> groups = new LinkedHashMap<>();

        for (GroupWithStudentIdDTO row : rows) {
            GroupDTO groupDTO = groups
                    .computeIfAbsent(
                            row.getGroupId(),
                            id -> new GroupDTO(id, row.getGroupName(), new ArrayList<>())
                    );
            if (row.getStudentId() != null) {
                groupDTO
                        .getStudentIdCollection()
                        .add(row.getStudentId());
            }
        }

        return groups
                .values()
                .stream()
                .toList();
    }
}
