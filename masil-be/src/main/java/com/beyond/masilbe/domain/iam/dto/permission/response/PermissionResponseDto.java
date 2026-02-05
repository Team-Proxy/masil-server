package com.beyond.masilbe.domain.iam.dto.permission.response;

import com.beyond.masilbe.domain.iam.entity.Permissions;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class PermissionResponseDto {

    private final Long permissionId;
    private final String permissionName;
    private final String permissionDescription;

    public static PermissionResponseDto fromEntity(final Permissions permission) {
        return PermissionResponseDto.builder()
                .permissionId(permission.getId())
                .permissionName(permission.getPermissionName())
                .permissionDescription(permission.getPermissionDescription())
                .build();
    }
}
