package org.sg01.example.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.*;
import org.sg01.example.lms.service.TeacherService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/teachers")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @PostMapping
    public ResponseEntity<TeacherDTO> create(@Valid @RequestBody TeacherCreateDTO request) {
        return ResponseEntity.ok(teacherService.create(request));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TeacherDTO> readById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TeacherDTO>> readAll() {
        return ResponseEntity.ok(teacherService.getAll());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TeacherDTO> update(@PathVariable Long id, @Valid @RequestBody TeacherUpdateDTO updateDTO) {
        return ResponseEntity.ok(teacherService.update(id, updateDTO));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        teacherService.delete(id);
    }
}
