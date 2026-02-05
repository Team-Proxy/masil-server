package com.beyond.masilbe.domain.iam.dto.permission.response;

import com.beyond.masilbe.domain.iam.entity.Permissions;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionListResponseDto {

    private final List<PermissionResponseDto> permissions;

    public static PermissionListResponseDto from(final List<Permissions> entities) {
        return PermissionListResponseDto.builder()
                .permissions(
                        entities.stream().map(PermissionResponseDto::fromEntity).toList())
                .build();
    }
}
