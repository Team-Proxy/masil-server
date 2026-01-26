package com.beyond.masilbe.domain.matching.controller;

import com.beyond.masilbe.domain.matching.dto.request.MatchStatusUpdateRequest;
import com.beyond.masilbe.domain.matching.dto.response.MatchListResponse;
import com.beyond.masilbe.domain.matching.enums.MatchQueryType;
import com.beyond.masilbe.domain.matching.service.MatchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "매칭")
@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchService matchService;

    @Operation(summary = "매칭 요청")
    @PostMapping
    public ResponseEntity<Long> request(@RequestParam Long requesterId, @RequestParam Long receiverId) {
        return ResponseEntity.ok(matchService.requestMatch(requesterId, receiverId));
    }

    @Operation(summary = "매칭 목록 조회")
    @GetMapping
    public ResponseEntity<MatchListResponse> getMatches(
            @RequestParam MatchQueryType type, @RequestParam Long memberId) {
        return ResponseEntity.ok(matchService.getMatches(type, memberId));
    }

    @Operation(summary = "매칭 상태 변경")
    @PatchMapping("/{matchId}")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long matchId, @RequestParam Long memberId, @RequestBody MatchStatusUpdateRequest request) {
        matchService.updateStatus(matchId, memberId, request.getStatus());
        return ResponseEntity.noContent().build();
    }
}
