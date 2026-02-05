package com.beyond.masilbe.domain.iam.entity;

import com.beyond.masilbe.common.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(name = "roles")
@AttributeOverride(name = "id", column = @Column(name = "role_id"))
public class Roles extends BaseEntity {

    @Column(name = "role_name", length = 50, nullable = false)
    private String roleName;

    @Column(name = "role_description", length = 255, nullable = false)
    private String roleDescription;

    // TODO: RoleCreateDto로 전달
    public static Roles create(final String roleName, final String roleDescription) {
        return Roles.builder()
                .roleName(roleName)
                .roleDescription(roleDescription)
                .build();
    }
}
