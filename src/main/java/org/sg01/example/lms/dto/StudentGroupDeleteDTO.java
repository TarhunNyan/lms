package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class StudentGroupDeleteDTO {

    private Long studentId;

    private Long groupId;

}
