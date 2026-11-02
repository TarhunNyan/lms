package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GroupToStudentIdDTO {

    private final Long groupId;

    private final Long studentId;

}