package org.sg01.example.lms.service;

import lombok.RequiredArgsConstructor;
import org.sg01.example.lms.dto.GroupCreateDTO;
import org.sg01.example.lms.dto.GroupDTO;
import org.sg01.example.lms.dto.GroupUpdateDTO;
import org.sg01.example.lms.dto.GroupWithStudentIdDTO;
import org.sg01.example.lms.entity.Group;
import org.sg01.example.lms.entity.Student;
import org.sg01.example.lms.exception.GroupHasStudentsException;
import org.sg01.example.lms.exception.StudentNotInGroupException;
import org.sg01.example.lms.helper.ExceptionHelperService;
import org.sg01.example.lms.mapper.GroupMapper;
import org.sg01.example.lms.repository.GroupRepository;
import org.sg01.example.lms.repository.StudentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {

    private final ExceptionHelperService exception;
    private final GroupRepository groupRepository;
    private final StudentRepository studentRepository;
    private final GroupMapper groupMapper;

    @Override
    @Transactional
    public GroupDTO create(GroupCreateDTO createDTO) {
        Group group = new Group();
        group.setGroupName(createDTO.getGroupName());

        List<Long> studentIdList = createDTO.getStudentIdList();
        if (studentIdList != null && !studentIdList.isEmpty()) {
            Set<Long> studentIdSet = new HashSet<>(studentIdList);
            Set<Long> foundedIdSet = studentRepository.existsAllByIdList(studentIdSet);
            if (studentIdSet.size() != foundedIdSet.size()) {
                studentIdSet.removeAll(foundedIdSet);
                throw exception.createExceptionNotFoundStudentListById(studentIdSet);
            }
            // немного магии, не ходим в БД за каждым студентом, а создаем прокси по id
            Set<Student> studentList = studentIdSet
                    .stream()
                    .map(studentRepository::getReferenceById)
                    .collect(Collectors.toSet());
            group.setStudentList(studentList);
        }
        groupRepository.save(group);

        return groupMapper.toDTO(group);
    }

    @Override
    @Transactional(readOnly = true)
    public GroupDTO getById(Long id) {
        Group group = groupRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(id));
        return groupMapper.toDTO(group);
    }

    @Override
    @Transactional(readOnly = true)
    public List<GroupDTO> getAll() {
        List<GroupWithStudentIdDTO> rows = groupRepository.findAllWithStudentIds();
        System.out.println(rows);
        return groupMapper.map(rows);
    }

    @Override
    @Transactional
    public GroupDTO update(Long id, GroupUpdateDTO updateDTO) {
        Group group = groupRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(id));

        if (updateDTO.getGroupName() != null) {
            group.setGroupName(updateDTO.getGroupName());
        }

        if (updateDTO.getStudentIdList() != null) {
            Set<Long> desiredIds = updateDTO.getStudentIdList();

            // проверяем что все студенты существуют
            Map<Long, Student> students = studentRepository
                    .findAllById(desiredIds)
                    .stream()
                    .collect(Collectors.toMap(Student::getId, Function.identity()));
            if (students.size() != desiredIds.size()) {
                Set<Long> difference = new HashSet<>(desiredIds);
                difference.removeAll(students.keySet());
                throw exception.createExceptionNotFoundStudentListById(difference);
            }

            Set<Long> currentIds = group
                    .getStudentList()
                    .stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());

            // проверяем что никто из студентов удаляемых из группы не останется без группы
            Set<Long> removedIds = new HashSet<>(currentIds);
            removedIds.removeAll(desiredIds);
            if (!removedIds.isEmpty() && !studentRepository.findStudentIdsWithSingleGroup(removedIds).isEmpty()) {
                throw new StudentNotInGroupException("Cannot update group id=%d: some students would be left without group".formatted(id));
            }

            // заменяем коллекцию целиком
            group
                    .getStudentList()
                    .clear();
            group
                    .getStudentList()
                    .addAll(students.values());
        }

        return groupMapper.toDTO(group);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Group group = groupRepository
                .findById(id)
                .orElseThrow(() -> exception.createExceptionNotFoundGroupById(id));
        List<Long> studentIds = group
                .getStudentList()
                .stream()
                .map(Student::getId)
                .toList();
        int countStudentWithOneGroup = studentRepository
                .findStudentIdsWithSingleGroup(studentIds)
                .size();
        if (countStudentWithOneGroup > 0) {
            throw new GroupHasStudentsException("Cannot remove group with id %d. %d students will be left without group".formatted(id, countStudentWithOneGroup));
        }
        group
                .getStudentList()
                .clear();
        groupRepository.delete(group);
    }

}
