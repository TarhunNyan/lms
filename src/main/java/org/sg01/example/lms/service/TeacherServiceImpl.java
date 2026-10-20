package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.*;
import org.sg01.example.lms.entity.Teacher;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.TeacherMapper;
import org.sg01.example.lms.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {

    private final ExceptionHelperService exception;
    private final TeacherRepository teacherRepository;
    private final TeacherMapper teacherMapper;

    @Override
    @Transactional
    public TeacherDTO create(TeacherCreateDTO createDTO) {
        Teacher teacher = new Teacher();
        teacher.setFirstname(createDTO.getFirstname());
        teacher.setLastname(createDTO.getLastname());
        teacherRepository.save(teacher);

        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public TeacherDTO getById(Long id) {
        Teacher teacher = teacherRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundTeacherById(id));
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TeacherDTO> getAll() {
        return teacherRepository
                .findAll()
                .stream()
                .map(teacherMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public TeacherDTO update(Long id, TeacherUpdateDTO updateDTO) {
        Teacher teacher = teacherRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundTeacherById(id));

        if (updateDTO.getFirstname() != null) {
            teacher.setFirstname(updateDTO.getFirstname());
        }
        if (updateDTO.getLastname() != null) {
            teacher.setLastname(updateDTO.getLastname());
        }

        teacherRepository.save(teacher);
        return teacherMapper.toDTO(teacher);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!teacherRepository.existsById(id)) {
            throw exception.createExceptionNotFoundTeacherById(id);
        }
        teacherRepository.deleteById(id);
    }

}
