package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class CourseCreateDTO {

    @NotBlank
    private String title;

    private String description;

    private Long teacherId;

}
