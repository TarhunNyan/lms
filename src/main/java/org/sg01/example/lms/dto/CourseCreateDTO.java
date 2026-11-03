package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CourseCreateDTO {

    @NotBlank
    private final String title;

    private final String description;

    private final Long teacherId;

}
