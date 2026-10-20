package org.sg01.example.lms.controller;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentGroupCreateDTO;
import org.sg01.example.lms.dto.StudentGroupDeleteDTO;
import org.sg01.example.lms.service.StudentGroupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/students/{id}/groups")
@RequiredArgsConstructor
public class StudentGroupController {

    private final StudentGroupService studentGroupService;

    @PostMapping("/{groupId}")
    public ResponseEntity<StudentDTO> addStudentToGroup(@PathVariable(name = "id") Long studentId, @PathVariable Long groupId) {
        return ResponseEntity.ok(studentGroupService.addStudentToGroup(new StudentGroupCreateDTO(studentId, groupId)));
    }

    @DeleteMapping("/{groupId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeGroup(@PathVariable(name = "id") Long studentId, @PathVariable Long groupId) {
        studentGroupService.removeStudentToGroup(new StudentGroupDeleteDTO(studentId, groupId));
    }

}
