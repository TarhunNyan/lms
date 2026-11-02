
package org.sg01.example.lms.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.CourseDTO;
import org.sg01.example.lms.dto.TeacherCourseCreateDTO;
import org.sg01.example.lms.dto.TeacherCourseDeleteDTO;
import org.sg01.example.lms.entity.Course;
import org.sg01.example.lms.entity.Teacher;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.CourseMapper;
import org.sg01.example.lms.repository.CourseRepository;
import org.sg01.example.lms.repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CourseTeacherServiceImpl implements CourseTeacherService {

    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final ExceptionHelperService exception;
    private final CourseMapper courseMapper;

    @Override
    @Transactional
    public CourseDTO addRelation(TeacherCourseCreateDTO requestDTO) {
        Long courseId = requestDTO.getCourseId();
        Long teacherId = requestDTO.getTeacherId();

        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> exception.createExceptionNotFoundCourseById(courseId));
        Teacher teacher = teacherRepository
                .findById(teacherId)
                .orElseThrow(() -> exception.createExceptionNotFoundCourseById(teacherId));
        course.setTeacher(teacher);
        courseRepository.save(course);

        return courseMapper.toDTO(course);
    }

    @Override
    @Transactional
    public void removeRelation(TeacherCourseDeleteDTO requestDTO) {
        Long courseId = requestDTO.getCourseId();

        Course course = courseRepository
                .findById(courseId)
                .orElseThrow(() -> exception.createExceptionNotFoundCourseById(courseId));
        course.setTeacher(null);
        courseRepository.save(course);
    }
}
