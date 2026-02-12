package com.beyond.masilbe.domain.matching.dto.response;

import com.beyond.masilbe.domain.matching.enums.MatchStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchResponse {

    private Long matchId;

    private Long opponentId;
    private String nickname;
    private Integer age;
    // TODO: 프로필은 후에 추가
    // private String profileImageUrl;

    private MatchStatus status;

    private Instant createdAt;
    private String timeAgo;
}
