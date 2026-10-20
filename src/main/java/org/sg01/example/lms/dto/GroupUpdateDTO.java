package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.sg01.example.lms.entity.Student;

import java.util.List;
import java.util.Set;

@Getter
@AllArgsConstructor
public class GroupUpdateDTO {

    private String groupName;

    private Set<Student> studentList;

}
