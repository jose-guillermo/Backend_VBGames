package com.vbgames.backend.matchservice.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.matchservice.entities.Match;

@Repository
public interface MatchRepository extends JpaRepository<Match, UUID> {

    @Query(
        value = """
        DELETE FROM matches
        WHERE id = :matchId
        RETURNING *
        """,
        nativeQuery = true
    )
    Match findMatchAndDelete(UUID matchId);
}
