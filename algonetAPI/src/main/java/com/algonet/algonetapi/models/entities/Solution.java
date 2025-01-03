package com.algonet.algonetapi.models.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "solutions")
@Getter
@Setter
public class Solution {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    private Integer problem_id;
    private Integer user_id;
    private Instant created_at;
    private String code;
    private Integer grade;
}
