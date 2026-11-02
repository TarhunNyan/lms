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
    private final Long groupId;

    private final String groupName;

    private final List<Long> studentIdCollection;

}