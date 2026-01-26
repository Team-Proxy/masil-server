package com.beyond.masilbe.domain.matching.service;

import static com.beyond.masilbe.domain.matching.enums.MatchStatus.REQUESTED;

import com.beyond.masilbe.common.util.TimeAgoUtils;
import com.beyond.masilbe.domain.iam.entity.Users;
import com.beyond.masilbe.domain.iam.repository.UserJpaRepository;
import com.beyond.masilbe.domain.matching.dto.response.MatchListResponse;
import com.beyond.masilbe.domain.matching.dto.response.MatchResponse;
import com.beyond.masilbe.domain.matching.entity.Matches;
import com.beyond.masilbe.domain.matching.enums.MatchQueryType;
import com.beyond.masilbe.domain.matching.enums.MatchStatus;
import com.beyond.masilbe.domain.matching.repository.MatchRepository;
import jakarta.transaction.Transactional;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MatchServiceImpl implements MatchService {

    private final MatchRepository matchRepository;
    private final UserJpaRepository userJpaRepository;

    @Transactional
    public Long requestMatch(Long requesterId, Long receiverId) {

        Matches matches = Matches.builder()
                .requesterId(requesterId)
                .receiverId(receiverId)
                .status(REQUESTED)
                .build();

        return matchRepository.save(matches).getId();
    }

    @Transactional
    public MatchListResponse getMatches(MatchQueryType type, Long memberId) {
        return switch (type) {
            case ARRIVED -> getArrivedMatches(memberId);
            case REQUESTED -> getRequestedMatches(memberId);
        };
    }

    private MatchListResponse getArrivedMatches(Long memberId) {

        long totalCount = matchRepository.countByReceiverIdAndStatus(memberId, REQUESTED);

        List<MatchResponse> matches = matchRepository.findByReceiverIdAndStatus(memberId, REQUESTED).stream()
                .map(match -> toMatchResponse(match, match.getRequesterId()))
                .toList();

        return MatchListResponse.builder()
                .totalCount((int) totalCount)
                .matches(matches)
                .build();
    }

    private MatchListResponse getRequestedMatches(Long memberId) {

        long totalCount = matchRepository.countByRequesterIdAndStatus(memberId, REQUESTED);

        List<MatchResponse> matches = matchRepository.findByRequesterIdAndStatus(memberId, REQUESTED).stream()
                .map(match -> toMatchResponse(match, match.getReceiverId()))
                .toList();

        return MatchListResponse.builder()
                .totalCount((int) totalCount)
                .matches(matches)
                .build();
    }

    @Transactional
    public void updateStatus(Long matchId, Long memberId, MatchStatus status) {

        Matches matches = find(matchId);

        switch (status) {
            case ACCEPTED -> {
                validateReceiver(matches, memberId);
                matches.accept();
            }
            case REJECTED -> {
                validateReceiver(matches, memberId);
                matches.reject();
            }
            case CANCELED -> {
                validateRequester(matches, memberId);
                matches.cancel();
            }
            default -> throw new IllegalArgumentException("변경할 수 없는 상태입니다.");
        }
    }

    private MatchResponse toMatchResponse(Matches matches, Long opponentId) {

        Users opponent = userJpaRepository
                .findById(opponentId)
                .orElseThrow(() -> new IllegalArgumentException("상대 회원이 존재하지 않습니다."));

        return MatchResponse.builder()
                .matchId(matches.getId())
                .opponentId(opponent.getId())
                .nickname(opponent.getNickname())
                .age(opponent.getAge())
                .status(matches.getStatus())
                .createdAt(matches.getCreatedAt())
                .timeAgo(TimeAgoUtils.toTimeAgo(matches.getCreatedAt()))
                .build();
    }

    private void validateReceiver(Matches matches, Long memberId) {
        if (!matches.getReceiverId().equals(memberId)) {
            throw new IllegalStateException("수신자만 처리할 수 있습니다.");
        }
    }

    private void validateRequester(Matches matches, Long memberId) {
        if (!matches.getRequesterId().equals(memberId)) {
            throw new IllegalStateException("요청자만 처리할 수 있습니다.");
        }
    }

    private Matches find(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(() -> new IllegalArgumentException("매칭이 존재하지 않습니다."));
    }
}
