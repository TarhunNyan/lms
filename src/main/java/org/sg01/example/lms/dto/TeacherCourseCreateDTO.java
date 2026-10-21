package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TeacherCourseCreateDTO {

    private Long teacherId;

    private Long courseId;

}
