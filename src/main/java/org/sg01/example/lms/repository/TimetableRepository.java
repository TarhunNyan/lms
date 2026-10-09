package org.sg01.example.lms.repository;

import org.sg01.example.lms.entity.Timetable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TimetableRepository extends JpaRepository<Timetable, Long> {
}
