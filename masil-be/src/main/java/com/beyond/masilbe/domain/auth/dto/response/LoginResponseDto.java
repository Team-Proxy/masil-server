package com.beyond.masilbe.domain.auth.dto.response;

import com.beyond.masilbe.domain.iam.entity.Users;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LoginResponseDto implements LoginResult {

    private Long userId;
    private String userName;
    private String email;
    private String accessToken;

    public static LoginResponseDto of(final Users users, final String accessToken) {
        return LoginResponseDto.builder()
                .userId(users.getId())
                .userName(users.getUserName())
                .email(users.getEmail())
                .accessToken(accessToken)
                .build();
    }
}
