package org.sg01.example.lms.dto;

import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class TimetableUpdateDTO {

    @Positive
    private final Long groupId;

    @Positive
    private final Long courseId;

    private final LocalDate startDate;

    private final LocalDate finishDate;

}
