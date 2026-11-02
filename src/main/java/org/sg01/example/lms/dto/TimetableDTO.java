package org.sg01.example.lms.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class TimetableDTO {

    private Long id;

    private Long version;

    private Long groupId;

    private Long courseId;

    private Long teacherId;

    private LocalDate startDate;

    private LocalDate finishDate;

}
