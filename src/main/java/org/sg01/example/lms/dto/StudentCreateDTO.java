package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudentCreateDTO {

    @NotBlank
    private String firstname;

    @NotBlank
    private String lastname;

    // мне не очень нравиться, но этого требует инвариант, что студент должен входить обязательно в 1 группу
    @NotNull
    @Positive
    private Long groupId;

}
