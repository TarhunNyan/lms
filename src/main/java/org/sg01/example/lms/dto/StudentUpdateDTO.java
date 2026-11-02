package org.sg01.example.lms.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class StudentUpdateDTO {

    private final String firstname;

    private final String lastname;

}
