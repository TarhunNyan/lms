package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudentDTO {

    private final Long id;

    private final String firstname;

    private final String lastname;

    private final List<Long> groupIdCollection;

}
