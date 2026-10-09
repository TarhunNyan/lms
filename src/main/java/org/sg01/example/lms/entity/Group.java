package org.sg01.example.lms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "student_groups")
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "group_name", nullable = false)
    private String groupName;

    @ManyToMany(mappedBy = "groups")
    private List<Student> student = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        version = 0L;
    }

}
