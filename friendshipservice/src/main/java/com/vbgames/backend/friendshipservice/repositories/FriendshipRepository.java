package com.vbgames.backend.friendshipservice.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.friendshipservice.dtos.FriendResponse;
import com.vbgames.backend.friendshipservice.entities.Friendship;
import com.vbgames.backend.friendshipservice.entities.FriendshipId;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    @Query(
        value =  """
        SELECT f.user_id_2 AS id, f.accepted AS accepted, u.username AS username
        FROM friendships f
        JOIN users u ON u.id = f.user_id_2
        WHERE f.user_id_1 = :userId

        UNION ALL

        SELECT f.user_id_1 AS id, f.accepted AS accepted, u.username AS username
        FROM friendships f
        JOIN users u ON u.id = f.user_id_1
        WHERE f.user_id_2 = :userId
        """,
        nativeQuery = true
    )
    List<FriendResponse> findAllFriendsByUserId(@Param("userId") UUID userId);
    
    @Query(
        value = """
        SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END
        FROM friendships
        WHERE (user_id_1 = :user1Id AND user_id_2 = :user2Id)
        OR (user_id_1 = :user2Id AND user_id_2 = :user1Id)
        """, 
        nativeQuery = true
    )
    Boolean existsByUsers(UUID user1Id, UUID user2Id);

    @Query(
        value = """
        SELECT *
        FROM friendships
        WHERE (user_id_1 = :user1Id AND user_id_2 = :user2Id)
        OR (user_id_1 = :user2Id AND user_id_2 = :user1Id)
        """,
        nativeQuery = true
    )
    Optional<Friendship> findBetweenUsers(UUID user1Id, UUID user2Id);

    @Modifying
    @Query(value = "DELETE FROM friendships WHERE accepted = false AND created_at < :timestamp", nativeQuery = true)
    void deleteExpiredFriendships(long timestamp);
}
    