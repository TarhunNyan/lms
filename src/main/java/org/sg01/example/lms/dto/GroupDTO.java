package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupDTO {

    @NotNull
    @Positive
    private Long groupId;

    private String groupName;

    private List<Long> studentIdCollection;

}