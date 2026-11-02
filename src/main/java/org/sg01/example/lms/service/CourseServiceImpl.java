package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.CourseCreateDTO;
import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.dto.CourseUpdateDTO;
import org.sg01.example.lms.entity.Course;
import org.sg01.example.lms.entity.Teacher;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.CourseMapper;
import org.sg01.example.lms.repository.CourseRepository;
import org.sg01.example.lms.repository.TeacherRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final ExceptionHelperService exception;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseDTO create(CourseCreateDTO createDTO) {
        Course course = new Course();
        course.setTitle(createDTO.getTitle());
        course.setDescription(Objects.requireNonNullElse(createDTO.getDescription(), ""));
        Teacher teacher = teacherRepository
                .findById(createDTO.getTeacherId())
                .orElseThrow(() -> exception.createExceptionNotFoundTeacherById(createDTO.getTeacherId()));
        course.setTeacher(teacher);
        courseRepository.save(course);

        return courseMapper.toDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseDTO getById(Long id) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundCourseById(id));
        return courseMapper.toDTO(course);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseDTO> getAll() {
        return courseRepository
                .findAll()
                .stream()
                .map(courseMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public CourseDTO update(Long id, CourseUpdateDTO updateDTO) {
        Course course = courseRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundCourseById(id));

        if (updateDTO.getTitle() != null) {
            course.setTitle(updateDTO.getTitle());
        }
        if (updateDTO.getDescription() != null) {
            course.setDescription(updateDTO.getDescription());
        }
        courseRepository.save(course);

        return courseMapper.toDTO(course);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!courseRepository.existsById(id)) {
            throw exception.createExceptionNotFoundCourseById(id);
        }
        courseRepository.deleteById(id);
    }

}
