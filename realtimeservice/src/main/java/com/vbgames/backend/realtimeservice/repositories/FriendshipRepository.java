package com.vbgames.backend.realtimeservice.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.vbgames.backend.realtimeservice.entities.Friendship;
import com.vbgames.backend.realtimeservice.entities.FriendshipId;

@Repository
public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipId> {

    @Query(
        value =  """
        SELECT f.user_id_2
        FROM friendships f
        WHERE f.user_id_1 = :userId

        UNION

        SELECT f.user_id_1
        FROM friendships f
        WHERE f.user_id_2 = :userId
        """,
        nativeQuery = true
    )
    List<UUID> findAllFriendsByUserId(@Param("userId") UUID userId);
}
    