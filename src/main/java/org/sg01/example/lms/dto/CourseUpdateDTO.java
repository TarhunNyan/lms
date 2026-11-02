package org.sg01.example.lms.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CourseUpdateDTO {

    private String title;

    private String description;

    @Positive
    private Long teacherId;

}
