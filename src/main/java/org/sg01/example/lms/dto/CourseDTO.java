package org.sg01.example.lms.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseDTO {

    private Long id;

    private String title;

    private String description;

    @Positive
    private Long teacherId;

}
