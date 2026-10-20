package org.sg01.example.lms.helper;

import lombok.NoArgsConstructor;
import org.sg01.example.lms.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@NoArgsConstructor
public class ExceptionHelperService {
    public static String NOT_FOUND_TEMPLATE = "Not found resource: %s by %s = '%s'";
    public static String NOT_FOUND_ARRYAY_TEMPLATE = "Not found resource: %s by %s. Not founded values: %s";

    public ResourceNotFoundException createExceptionNotFoundCourseById(Long id) {
        return new ResourceNotFoundException(NOT_FOUND_TEMPLATE.formatted("course", "id", id));
    }

    public ResourceNotFoundException createExceptionNotFoundStudentById(Long id) {
        return new ResourceNotFoundException(NOT_FOUND_TEMPLATE.formatted("student", "id", id));
    }

    public ResourceNotFoundException createExceptionNotFoundStudentListById(Collection<Long> idList) {
        String values = idList
                .stream()
                .map(Objects::toString)
                .collect(Collectors.joining());
        return new ResourceNotFoundException(NOT_FOUND_ARRYAY_TEMPLATE.formatted("student", "id", values));
    }

    public ResourceNotFoundException createExceptionNotFoundGroupById(Long id) {
        return new ResourceNotFoundException(NOT_FOUND_TEMPLATE.formatted("group", "id", id));
    }

    public ResourceNotFoundException createExceptionNotFoundTeacherById(Long id) {
        return new ResourceNotFoundException(NOT_FOUND_TEMPLATE.formatted("teacher", "id", id));
    }

    public ResourceNotFoundException createExceptionNotFoundTimetableById(Long id) {
        return new ResourceNotFoundException(NOT_FOUND_TEMPLATE.formatted("timetable", "id", id));
    }

}
