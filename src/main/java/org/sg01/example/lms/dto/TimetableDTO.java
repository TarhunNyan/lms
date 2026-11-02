package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TimetableDTO {

    private final Long id;

    private final Long version;

    private final Long groupId;

    private final Long courseId;

    private final Long teacherId;

    private final LocalDate startDate;

    private final LocalDate finishDate;

}
