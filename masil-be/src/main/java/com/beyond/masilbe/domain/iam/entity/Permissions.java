package com.beyond.masilbe.domain.iam.entity;

import com.beyond.masilbe.common.entity.BaseEntity;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Table(
        name = "permissions",
        indexes = {@Index(name = "uk_permissions_name", columnList = "permission_name", unique = true)})
@AttributeOverride(name = "id", column = @Column(name = "permission_id"))
@SQLRestriction("deleted_at IS NULL")
public class Permissions extends BaseEntity {

    @Column(name = "permission_name", length = 100, nullable = false)
    private String permissionName;

    @Column(name = "permission_description", length = 255)
    private String permissionDescription;

    public static Permissions create(final String permissionName, final String permissionDescription) {
        return Permissions.builder()
                .permissionName(permissionName)
                .permissionDescription(permissionDescription)
                .build();
    }
}
