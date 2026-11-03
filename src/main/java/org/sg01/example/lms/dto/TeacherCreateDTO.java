package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class TeacherCreateDTO {

    @NotBlank
    private final String firstname;

    @NotBlank
    private final String lastname;

}
