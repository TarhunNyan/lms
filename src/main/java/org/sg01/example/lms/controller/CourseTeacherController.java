package org.sg01.example.lms.controller;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.TeacherCourseCreateDTO;
import org.sg01.example.lms.dto.TeacherCourseDeleteDTO;
import org.sg01.example.lms.service.CourseTeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/course/{courseId}/teacher")
@RequiredArgsConstructor
public class CourseTeacherController {

    private final CourseTeacherService courseTeacherService;

    @PostMapping("/{teacherId}")
    public ResponseEntity<CourseDTO> addRelation(@PathVariable Long teacherId, @PathVariable Long courseId) {
        return ResponseEntity.ok(courseTeacherService.addRelation(new TeacherCourseCreateDTO(teacherId, courseId)));
    }

    @DeleteMapping("/")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeRelation(@PathVariable Long courseId) {
        courseTeacherService.removeRelation(new TeacherCourseDeleteDTO(courseId));
    }

}
