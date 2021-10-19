package com.example.ipldashboard.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Entity
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String teamName;
    private Long totalMatches;
    private Long totalWins;

    public Team(String teamName, Long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
}
