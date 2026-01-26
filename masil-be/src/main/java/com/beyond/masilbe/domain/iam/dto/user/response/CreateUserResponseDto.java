package com.beyond.masilbe.domain.iam.dto.user.response;

import com.beyond.masilbe.domain.iam.entity.Users;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class CreateUserResponseDto {
    private final Long userId;
    private final String userName;
    private final String nickname;
    private final String email;
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
