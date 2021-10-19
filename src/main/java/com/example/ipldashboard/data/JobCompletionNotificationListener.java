package com.example.ipldashboard.data;

import com.example.ipldashboard.model.Team;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class JobCompletionNotificationListener extends JobExecutionListenerSupport {

    private static final Logger log = LoggerFactory.getLogger(JobCompletionNotificationListener.class);

    private final EntityManager entityManager;

    @Override
    @Transactional
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            log.info("!!! JOB FINISHED!  Time to verify the results.");

            Map<String, Team> teamData = new HashMap<>();
            entityManager.createQuery("SELECT m.team1, COUNT(m) FROM Match m GROUP BY m.team1", Object[].class)
                    .getResultList()
                    .stream()
                    .map(e -> new Team(String.valueOf(e[0]), Long.valueOf(e[1].toString())))
                    .forEach(team -> teamData.put(team.getTeamName(), team));

            entityManager.createQuery("SELECT m.team2, COUNT(m) FROM Match m GROUP BY m.team2", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get(String.valueOf(e[0]));
                        Long totalMatches = team.getTotalMatches() + Long.valueOf(e[1].toString());

                        team.setTotalMatches(totalMatches);
                    });

            entityManager.createQuery("SELECT m.matchWinner, COUNT(m) FROM Match m GROUP BY m.matchWinner", Object[].class)
                    .getResultList()
                    .stream()
                    .forEach(e -> {
                        Team team = teamData.get(String.valueOf(e[0]));

                        if (team != null) {
                            team.setTotalWins(Long.valueOf(e[1].toString()));
                        }
                    });

            teamData.values().forEach(team -> entityManager.persist(team));
            teamData.values().forEach(team -> System.out.println(team));
        }
    }
}
