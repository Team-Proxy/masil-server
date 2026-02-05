package com.beyond.masilbe.domain.iam.dto.role.response;

import com.beyond.masilbe.domain.iam.entity.Roles;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class RoleResponseDto {

    private final Long roleId;
    private final String roleName;
    private final String roleDescription;

    public static RoleResponseDto fromEntity(final Roles role) {
        return RoleResponseDto.builder()
                .roleId(role.getId())
                .roleName(role.getRoleName())
                .roleDescription(role.getRoleDescription())
                .build();
    }
}
