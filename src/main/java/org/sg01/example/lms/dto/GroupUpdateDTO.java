package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Set;

@Getter
@AllArgsConstructor
public class GroupUpdateDTO {

    private final String groupName;

    private final Set<Long> studentIdCollection;

}
