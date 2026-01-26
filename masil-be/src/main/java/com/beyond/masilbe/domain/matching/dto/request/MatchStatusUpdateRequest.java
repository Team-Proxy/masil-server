package com.beyond.masilbe.domain.matching.dto.request;

import com.beyond.masilbe.domain.matching.enums.MatchStatus;
import lombok.Getter;

@Getter
public class MatchStatusUpdateRequest {
    private MatchStatus status;
}
