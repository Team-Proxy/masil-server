package com.beyond.masilbe.domain.matching.dto.response;

import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MatchListResponse {
    private int totalCount;
    private List<MatchResponse> matches;
}
