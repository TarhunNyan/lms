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
    private Long groupId;

    @NonNull
    @Positive
    private Long courseId;

    @NonNull
    private LocalDate startDate;

    @NonNull
    private LocalDate finishDate;

}
