package org.sg01.example.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class GroupCreateDTO {

    @NotBlank
    private String groupName;

    private List<Long> studentIdCollection;

}
