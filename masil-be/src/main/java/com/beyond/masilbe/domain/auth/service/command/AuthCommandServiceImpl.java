package com.beyond.masilbe.domain.auth.service.command;

import com.beyond.masilbe.domain.auth.dto.request.LoginRequestDto;
import com.beyond.masilbe.domain.auth.dto.response.LoginResponseDto;
import com.beyond.masilbe.domain.auth.dto.response.LoginServiceResult;
import com.beyond.masilbe.domain.auth.exception.AuthException;
import com.beyond.masilbe.domain.iam.entity.Users;
import com.beyond.masilbe.domain.iam.exception.UserException;
import com.beyond.masilbe.domain.iam.support.user.UserReader;
import com.beyond.masilbe.internal.auth.dto.TokenPairDto;
import com.beyond.masilbe.security.jwt.JwtTokenProvider;
import com.beyond.masilbe.security.jwt.RedisTokenRepository;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final UserReader userReader;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisTokenRepository redisTokenRepository;

    @Override
    @Transactional
    public LoginServiceResult login(final LoginRequestDto request) {

        final Users users = getUserOrThrow(request.getLoginKey());
        validatePassword(request.getPassword(), users.getPassword());
        final Long userId = users.getId();

        // access + refresh 발급
        TokenPairDto tokens = issueTokenPair(userId, users.getEmail());

        return new LoginServiceResult(LoginResponseDto.of(users, tokens.getAccess()), tokens.getRefresh());
    }

    @Override
    @Transactional
    public void logout(final String accessToken) {

        final Long userId = jwtTokenProvider.getUserId(accessToken);

        // 리프레시 토큰 폐기
        redisTokenRepository.deleteRefreshToken(userId);

        long expiresIn = jwtTokenProvider.getRemainingValidityMillis(accessToken);
        redisTokenRepository.blacklistAccessToken(accessToken, Duration.ofMillis(expiresIn));
    }

    @Override
    @Transactional
    public LoginServiceResult refresh(final String refreshToken) {

        validateRefreshToken(refreshToken);

        final Long userId = jwtTokenProvider.getUserId(refreshToken);
        final Users users = userReader.findById(userId);

        // access + refresh rotate 발급 (기존 refresh 폐기 → 새로운 refresh 저장)
        TokenPairDto newTokens = issueTokenPair(userId, users.getEmail());

        return new LoginServiceResult(LoginResponseDto.of(users, newTokens.getAccess()), newTokens.getRefresh());
    }

    // -----------------------
    // 헬퍼 메서드

    // 이메일로 유저 검증
    private Users getUserOrThrow(final String loginKey) {
        return userReader.findByEmail(loginKey);
    }

    // 리프레시 토큰 검증
    private void validateRefreshToken(final String refreshToken) {
        if (!jwtTokenProvider.validateRefreshToken(refreshToken)) {
            throw AuthException.tokenExpired();
        }

        final Long userId = jwtTokenProvider.getUserId(refreshToken);
        final String storedToken = redisTokenRepository.getRefreshToken(userId);

        if (!refreshToken.equals(storedToken)) {
            throw AuthException.unauthorized();
        }
    }

    // 비밀번호 일치 여부 검증
    private void validatePassword(final String raw, final String encoded) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw UserException.invalidPassword();
        }
    }

    // AccessToken + RefreshToken 동시 발급
    private TokenPairDto issueTokenPair(final Long userId, final String email) {
        final String access = jwtTokenProvider.generateAccessToken(userId, email);
        final String refresh = jwtTokenProvider.generateRefreshToken(userId);

        redisTokenRepository.saveRefreshToken(
                userId, refresh, Duration.ofMillis(jwtTokenProvider.getRefreshTokenValidityMillis()));

        return TokenPairDto.of(access, refresh);
    }
}
