package org.sg01.example.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.*;
import org.sg01.example.lms.service.CourseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    public ResponseEntity<CourseDTO> create(@Valid @RequestBody CourseCreateDTO request) {
        return ResponseEntity.ok(courseService.create(request));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<CourseDTO> readById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(courseService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<CourseDTO>> readAll() {
        return ResponseEntity.ok(courseService.getAll());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<CourseDTO> update(@PathVariable Long id, @Valid @RequestBody CourseUpdateDTO updateDTO) {
        return ResponseEntity.ok(courseService.update(id, updateDTO));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        courseService.delete(id);
    }
}
