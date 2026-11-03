package org.sg01.example.lms.dto;

import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CourseDTO {

    private final Long id;

    private final String title;

    private final String description;

    @Positive
    private final Long teacherId;

}
