package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class StudentDTO {

    private Long id;

    private String firstname;

    private String lastname;

    private List<Long> groupIdCollection;

}
