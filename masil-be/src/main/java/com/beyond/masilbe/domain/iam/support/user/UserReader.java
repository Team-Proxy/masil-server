package com.beyond.masilbe.domain.iam.support.user;

import com.beyond.masilbe.domain.iam.entity.Users;
import com.beyond.masilbe.domain.iam.exception.UserException;
import com.beyond.masilbe.domain.iam.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReader {

    private final UserJpaRepository userJpaRepository;

    // pk기반 유저 조회
    public Users findById(final Long userId) {
        return userJpaRepository.findById(userId).orElseThrow(UserException::userNotFound);
    }

    // 이메일 기반 유저 조회
    public Users findByEmail(final String email) {
        return userJpaRepository.findByEmail(email).orElseThrow(UserException::userNotFound);
    }
}
