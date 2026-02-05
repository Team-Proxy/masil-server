package com.beyond.masilbe.domain.iam.dto.role.response;

import com.beyond.masilbe.domain.iam.entity.Roles;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class RoleListResponseDto {

    private final List<RoleResponseDto> roles;

    public static RoleListResponseDto from(final List<Roles> entities) {
        return RoleListResponseDto.builder()
                .roles(entities.stream().map(RoleResponseDto::fromEntity).toList())
                .build();
    }
}
