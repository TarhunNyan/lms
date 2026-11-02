package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.TimetableCreateDTO;
import org.sg01.example.lms.dto.TimetableDTO;
import org.sg01.example.lms.dto.TimetableUpdateDTO;
import org.sg01.example.lms.entity.Course;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Timetable;
import org.sg01.example.lms.exception.InvalidDateRangeException;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.TimetableMapper;
import org.sg01.example.lms.repository.CourseRepository;
import org.sg01.example.lms.repository.GroupRepository;
import org.sg01.example.lms.repository.TimetableRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TimetableServiceImpl implements TimetableService {

    private final ExceptionHelperService exception;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TimetableRepository timetableRepository;
    private final TimetableMapper timetableMapper;

    @Override
    @Transactional
    public TimetableDTO create(TimetableCreateDTO createDTO) {
        validateDateRange(createDTO.getStartDate(), createDTO.getFinishDate());

        Timetable timetable = new Timetable();

        Group group = groupRepository
                .findById(createDTO.getGroupId())
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(createDTO.getGroupId()));
        Course course = courseRepository
                .findById(createDTO.getCourseId())
                .orElseThrow(() -> exception.createExceptionNotFoundCourseById(createDTO.getCourseId()));
        timetable.setGroup(group);
        timetable.setCourse(course);
        timetable.setFinishDate(createDTO.getFinishDate());
        timetable.setStartDate(createDTO.getStartDate());
        timetableRepository.save(timetable);

        return timetableMapper.toDTO(timetable);
    }

    @Override
    @Transactional(readOnly = true)
    public TimetableDTO getById(Long id) {
        Timetable timetable = timetableRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundTimetableById(id));
        return timetableMapper.toDTO(timetable);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TimetableDTO> getAll() {
        return timetableRepository
                .findAll()
                .stream()
                .map(timetableMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public TimetableDTO update(Long id, TimetableUpdateDTO updateDTO) {
        Timetable timetable = timetableRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundTimetableById(id));

        LocalDate currentStartDate = Objects.requireNonNullElse(updateDTO.getStartDate(), timetable.getStartDate());
        LocalDate currentFinishDate = Objects.requireNonNullElse(updateDTO.getFinishDate(), timetable.getFinishDate());
        validateDateRange(currentStartDate, currentFinishDate);
        timetable.setStartDate(currentStartDate);
        timetable.setFinishDate(currentFinishDate);

        Long courseId = updateDTO.getCourseId();
        if (courseId != null) {
            Course courseById = courseRepository
                    .findById(courseId)
                    .orElseThrow(() -> exception.createExceptionNotFoundCourseById(courseId));
            timetable.setCourse(courseById);
        }

        Long groupId = updateDTO.getGroupId();
        if (groupId != null) {
            Group groupById = groupRepository
                    .findById(groupId)
                    .orElseThrow(() -> exception.createExceptionNotFoundGroupById(groupId));
            timetable.setGroup(groupById);
        }

        timetableRepository.save(timetable);
        return timetableMapper.toDTO(timetable);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        if (!timetableRepository.existsById(id)) {
            throw exception.createExceptionNotFoundTimetableById(id);
        }
        timetableRepository.deleteById(id);
    }

//    @Override
//    public List<TimetableDTO> findByGroup(Long groupId, Pageable pageable) {
//        return List.of();
//    }

    private void validateDateRange(LocalDate start, LocalDate finish) {
        if (start == null || finish == null) {
            throw new InvalidDateRangeException("Dates must not be null");
        }
        if (start.isAfter(finish)) {
            throw new InvalidDateRangeException("Timetable date range error. startDate=%s, finishDate=%s".formatted(start, finish));
        }
    }

}
