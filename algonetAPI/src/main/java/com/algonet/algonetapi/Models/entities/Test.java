package com.algonet.algonetapi.Models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tests")
@Getter
@Setter
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer problem_id;
    private String input;
    private String output;
}
