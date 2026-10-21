package org.sg01.example.lms.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "students")
@Getter
@Setter
@NoArgsConstructor
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "firstname", nullable = false)
    private String firstname;

    @Column(name = "lastname", nullable = false)
    private String lastname;

    @ManyToMany(mappedBy = "studentCollection", fetch = FetchType.LAZY)
    private Set<Group> groupCollection = new HashSet<>();

    public boolean addGroup(Group group) {
        groupCollection.add(group);
        return group.getStudentCollection().add(this);
    }

    public boolean removeGroup(Group group) {
        groupCollection.remove(group);
        return group.getStudentCollection().remove(this);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    // переопределяем equals так, чтобы он не делал лишних запросов к БД
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Student other)) {
            return false;
        }
        return id != null && id.equals(other.id);
    }

}
