package org.sg01.example.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.query.Page;
import org.sg01.example.lms.dto.*;
import org.sg01.example.lms.service.GroupService;
import org.sg01.example.lms.service.TimetableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.awt.print.Pageable;
import java.util.List;

@RestController
@RequestMapping("/api/groups")
@RequiredArgsConstructor
public class GroupController {

    private final GroupService groupService;
    private final TimetableService timetableService;

    @PostMapping
    public ResponseEntity<GroupDTO> create(@Valid @RequestBody GroupCreateDTO request) {
        return ResponseEntity.ok(groupService.create(request));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<GroupDTO> readById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(groupService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<GroupDTO>> readAll() {
        return ResponseEntity.ok(groupService.getAll());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<GroupDTO> update(@PathVariable Long id, @Valid @RequestBody GroupUpdateDTO updateDTO) {
        return ResponseEntity.ok(groupService.update(id, updateDTO));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        groupService.delete(id);
    }

//    @GetMapping("/{groupId}/timetables")
//    public Page<TimetableDTO> groupSchedule(@PathVariable Long groupId, Pageable pageable) {
//        return timetableService.findByGroup(groupId, pageable);
//    }

}
