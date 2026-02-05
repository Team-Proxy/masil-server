package com.beyond.masilbe.domain.iam.dto.user.response;

import com.beyond.masilbe.domain.iam.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * 회원가입 dto, 추후 UserReseponseDto와 합치거나 명명 변경 예정
 */
@Getter
@Builder
@AllArgsConstructor
public class CreateUserResponseDto {
    private final Long userId;
    private final String userName;
    private final String nickname;
    private final String email;
    // TODO: 추후 비밀번호는 응답에서 제거
    private final String password;

    public static CreateUserResponseDto fromEntity(final Users users) {
        return CreateUserResponseDto.builder()
                .userId(users.getId())
                .userName(users.getUserName())
                .nickname(users.getNickname())
                .email(users.getEmail())
                .password(users.getPassword())
                .build();
    }
}
