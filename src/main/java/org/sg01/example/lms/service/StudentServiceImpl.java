package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.StudentCreateDTO;
import org.sg01.example.lms.dto.StudentDTO;
import org.sg01.example.lms.dto.StudentUpdateDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.StudentMapper;
import org.sg01.example.lms.repository.GroupRepository;
import org.sg01.example.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final ExceptionHelperService exception;
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper studentMapper;

    @Override
    @Transactional
    public StudentDTO create(StudentCreateDTO createDTO) {
        Student student = new Student();

        Group group = groupRepository
                .findById(createDTO.getGroupId())
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(createDTO.getGroupId()));
        student.addGroup(group);
        student.setFirstname(createDTO.getFirstname());
        student.setLastname(createDTO.getLastname());
        studentRepository.save(student);

        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public StudentDTO getById(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundStudentById(id));
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional(readOnly = true)
    public List<StudentDTO> getAll() {
        return studentRepository
                .findAll()
                .stream()
                .map(studentMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public StudentDTO update(Long id, StudentUpdateDTO updateDTO) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundStudentById(id));

        if (updateDTO.getFirstname() != null) {
            student.setFirstname(updateDTO.getFirstname());
        }
        if (updateDTO.getLastname() != null) {
            student.setLastname(updateDTO.getLastname());
        }

        studentRepository.save(student);
        return studentMapper.toDTO(student);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundStudentById(id));

        // TODO: тут N+1
        for (Group group : student.getGroupList()) {
            group
                    .getStudentList()
                    .remove(student);
        }

        studentRepository.delete(student);
    }

}
