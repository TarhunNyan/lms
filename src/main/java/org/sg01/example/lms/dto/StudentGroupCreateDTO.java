package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentGroupCreateDTO {

    private final Long studentId;

    private final Long groupId;

}
