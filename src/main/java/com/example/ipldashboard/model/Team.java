package com.example.ipldashboard.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String teamName;
    private Long totalMatches;
    private Long totalWins;

    @Transient
    private List<Match> matches;

    public Team(String teamName, Long totalMatches) {
        this.teamName = teamName;
        this.totalMatches = totalMatches;
    }
}
