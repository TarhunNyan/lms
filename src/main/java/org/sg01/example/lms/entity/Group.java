package org.sg01.example.lms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;

import java.util.HashSet;
import java.util.Set;

@Table(name = "student_groups")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @BatchSize(size = 50)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "student_groups_students",
            joinColumns = @JoinColumn(name = "group_id"),
            inverseJoinColumns = @JoinColumn(name = "student_id")
    )
    private Set<Student> studentList = new HashSet<>();

    // переопределяем equals так, чтобы он не делал лишних запросов к БД
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Group other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

}
