package org.sg01.example.lms.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class TimetableUpdateDTO {

    @Positive
    private Long groupId;

    @Positive
    private Long courseId;

    private LocalDate startDate;

    private LocalDate finishDate;

}
