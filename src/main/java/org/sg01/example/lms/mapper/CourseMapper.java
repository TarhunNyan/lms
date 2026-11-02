package org.sg01.example.lms.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.entity.Course;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    //надо проверить как это с null работатет
    @Mapping(target = "teacherId", source = "teacher.id")
    CourseDTO toDTO(Course entity);

//    default Long map(Teacher teacher) {
//        if (teacher == null) {
//            return null;
//        }
//        return teacher.getId();
//    }

}
