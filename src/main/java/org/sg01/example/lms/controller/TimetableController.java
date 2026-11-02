package org.sg01.example.lms.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.*;
import org.sg01.example.lms.service.TimetableService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/timetables")
@RequiredArgsConstructor
public class TimetableController {

    private final TimetableService timetableService;

    @PostMapping
    public ResponseEntity<TimetableDTO> create(@Valid @RequestBody TimetableCreateDTO request) {
        return ResponseEntity.ok(timetableService.create(request));
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<TimetableDTO> readById(@Valid @PathVariable Long id) {
        return ResponseEntity.ok(timetableService.getById(id));
    }

    @GetMapping
    public ResponseEntity<List<TimetableDTO>> readAll() {
        return ResponseEntity.ok(timetableService.getAll());
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TimetableDTO> update(@PathVariable Long id, @Valid @RequestBody TimetableUpdateDTO updateDTO) {
        return ResponseEntity.ok(timetableService.update(id, updateDTO));
    }

    @DeleteMapping(path = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        timetableService.delete(id);
    }
}
