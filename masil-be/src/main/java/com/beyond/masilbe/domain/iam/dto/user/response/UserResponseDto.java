package com.beyond.masilbe.domain.iam.dto.user.response;

import com.beyond.masilbe.domain.iam.entity.Users;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserResponseDto {

    private final Long userId;
    private final String userName;
    private final String nickname;
    private final String email;
    private final Integer age;

    public static UserResponseDto fromEntity(final Users user) {
        return UserResponseDto.builder()
                .userId(user.getId())
                .userName(user.getUserName())
                .nickname(user.getNickname())
                .email(user.getEmail())
                .age(user.getAge())
                .build();
    }
}
