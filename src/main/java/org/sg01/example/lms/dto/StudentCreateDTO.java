package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudentCreateDTO {

    @NotBlank(message = "Student firstname is required and not blank")
    private String firstname;

    @NotBlank(message = "Student lastname is required and not blank")
    private String lastname;

}
