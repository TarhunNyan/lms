package org.sg01.example.lms.dto;

import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
@RequiredArgsConstructor
public class TimetableCreateDTO {

    @NonNull
    @Positive
    private final Long groupId;

    @NonNull
    @Positive
    private final Long courseId;

    @NonNull
    private final LocalDate startDate;

    @NonNull
    private final LocalDate finishDate;

}
