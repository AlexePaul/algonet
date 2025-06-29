package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tags")
@Getter
@Setter
public class Tag {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @NonNull    @Column(nullable = false)
    private String name;

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<Problem> problems = new ArrayList<>();

    @ManyToMany(mappedBy = "tags", fetch = FetchType.LAZY)
    private List<User> users = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Tag tag = (Tag) o;
        return id.equals(tag.id) &&
                name.equals(tag.name);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
