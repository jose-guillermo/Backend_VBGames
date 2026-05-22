package com.vbgames.backend.matchservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.matchservice.entities.MatchQueue;
import com.vbgames.backend.matchservice.entities.MatchQueueId;

@Repository
public interface MatchQueueRepository extends JpaRepository<MatchQueue, MatchQueueId> {

    List<MatchQueue> findAllByGameId(UUID gameId);

    // Consulta para encontrar un match y borrarlo, no permite que otro usuario lo tome
    @Query(
        value = """
        DELETE FROM match_queue 
        WHERE (user_id, game_id) = (
            SELECT user_id, game_id FROM match_queue 
            WHERE game_id = :gameId 
                AND rating BETWEEN (:rating - 150) AND (:rating + 150)
            ORDER BY created_at ASC
            LIMIT 1
            FOR UPDATE SKIP LOCKED 
        )
        RETURNING *
        """,
        nativeQuery = true
    )
    MatchQueue findMatchAndDelete(UUID gameId, short rating);

    void deleteAllByUserId(UUID userId);

    int deleteAllByUserIdAndGameId(UUID userId, UUID gameId);
}
