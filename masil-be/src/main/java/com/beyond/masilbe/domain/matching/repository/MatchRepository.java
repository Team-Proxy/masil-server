package com.beyond.masilbe.domain.matching.repository;

import com.beyond.masilbe.domain.matching.entity.Matches;
import com.beyond.masilbe.domain.matching.enums.MatchStatus;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MatchRepository extends JpaRepository<Matches, Long> {

    List<Matches> findByReceiverIdAndStatus(Long receiverId, MatchStatus status);

    List<Matches> findByRequesterIdAndStatus(Long requesterId, MatchStatus status);

    long countByReceiverIdAndStatus(Long receiverId, MatchStatus status);

    long countByRequesterIdAndStatus(Long requesterId, MatchStatus status);
}
