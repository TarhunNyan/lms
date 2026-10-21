package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentGroupCreateDTO;
import org.sg01.example.lms.dto.StudentGroupDeleteDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.exception.LastGroupRemovalException;
import org.sg01.example.lms.exception.StudentAlreadyInGroupException;
import org.sg01.example.lms.exception.StudentNotInGroupException;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.StudentMapper;
import org.sg01.example.lms.repository.GroupRepository;
import org.sg01.example.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudentGroupServiceImpl implements StudentGroupService {

    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final ExceptionHelperService exception;
    private final StudentMapper studentMapper;

    @Override
    public StudentDTO addStudentToGroup(StudentGroupCreateDTO requestDTO) {
        Long groupId = requestDTO.getGroupId();
        Long studentId = requestDTO.getStudentId();

        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(groupId));
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> exception.createExceptionNotFoundStudentById(studentId));
        if (!student.addGroup(group)) {
            throw new StudentAlreadyInGroupException("Student %d is already in group %d".formatted(studentId, groupId));
        }

        studentRepository.save(student);
        return studentMapper.toDTO(student);
    }

    @Override
    public void removeStudentToGroup(StudentGroupDeleteDTO requestDTO) {
        Long groupId = requestDTO.getGroupId();
        Long studentId = requestDTO.getStudentId();

        Group group = groupRepository
                .findById(groupId)
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(groupId));
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> exception.createExceptionNotFoundStudentById(studentId));
        if (!student.getGroupList().contains(group)) {
            throw new StudentNotInGroupException("Student %d is not in group %d".formatted(studentId, groupId));
        }
        if (student.getGroupList().size() == 1) {
            throw new LastGroupRemovalException("Cannot remove student %d from the last group %d".formatted(studentId, groupId));
        }

        student.removeGroup(group);
        groupRepository.save(group);
    }
}
