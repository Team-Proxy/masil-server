package com.beyond.masilbe.domain.matching.service;

import com.beyond.masilbe.domain.matching.dto.response.MatchListResponse;
import com.beyond.masilbe.domain.matching.enums.MatchQueryType;
import com.beyond.masilbe.domain.matching.enums.MatchStatus;

public interface MatchService {
    Long requestMatch(Long requesterId, Long receiverId);

    MatchListResponse getMatches(MatchQueryType type, Long memberId);

    void updateStatus(Long matchId, Long memberId, MatchStatus status);
}
