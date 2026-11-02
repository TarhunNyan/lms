package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class GroupWithStudentIdDTO {

    private Long groupId;

    private String groupName;

    private Long studentId;

}