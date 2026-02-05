package com.beyond.masilbe.domain.iam.dto.user.response;

import com.beyond.masilbe.domain.iam.entity.Users;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserListResponseDto {

    private final List<UserResponseDto> users;

    public static UserListResponseDto from(final List<Users> entities) {
        return UserListResponseDto.builder()
                .users(entities.stream().map(UserResponseDto::fromEntity).toList())
                .build();
    }
}
