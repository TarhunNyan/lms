package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sg01.example.lms.dto.TimetableDTO;
import org.sg01.example.lms.entity.Timetable;

@Mapper(componentModel = "spring")
public interface TimetableMapper {

    @Mapping(target = "groupId", source = "group.id")
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "teacherId", source = "course.teacher.id")
    TimetableDTO toDTO(Timetable entity);

}