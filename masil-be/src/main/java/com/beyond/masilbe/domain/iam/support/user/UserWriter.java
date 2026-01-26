package com.beyond.masilbe.domain.iam.support.user;

import com.beyond.masilbe.domain.iam.entity.Users;
import com.beyond.masilbe.domain.iam.exception.UserException;
import com.beyond.masilbe.domain.iam.repository.UserJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserWriter {

    private final UserJpaRepository userJpaRepository;

    // user 저장
    public Users save(final Users users) {
        try {
            return userJpaRepository.save(users);
        } catch (DataIntegrityViolationException e) {

            // RDB Duplicate Key 에러 메시지 매핑
            final String msg = e.getMostSpecificCause().getMessage();

            if (msg.contains("uk_user_no_active")) {
                throw UserException.userAlreadyExists();
            }
            if (msg.contains("uk_user_email_active")) {
                throw UserException.userAlreadyExists();
            }

            // 예상 못한 DB 제약이면 그대로 던짐
            throw e;
        }
    }
}
