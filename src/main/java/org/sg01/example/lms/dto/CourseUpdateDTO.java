package org.sg01.example.lms.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CourseUpdateDTO {

    private final String title;

    private final String description;

    @Positive
    private final Long teacherId;

}
