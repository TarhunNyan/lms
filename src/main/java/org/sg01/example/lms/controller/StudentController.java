package org.sg01.example.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequiredArgsConstructor
public class StudentController {

    final StudentService studentService;

    @GetMapping(path = "/api/students/{id}")
    public ResponseEntity<StudentDTO> readById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(studentService.getById(id));
    }

}
