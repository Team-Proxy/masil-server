package com.beyond.masilbe.domain.iam.repository;

import com.beyond.masilbe.domain.iam.entity.Permissions;
import com.beyond.masilbe.domain.iam.entity.RolePermissions;
import com.beyond.masilbe.domain.iam.entity.Roles;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RolePermissionJpaRepository extends JpaRepository<RolePermissions, Long> {

    List<RolePermissions> findAllByRole(final Roles role);

    List<RolePermissions> findAllByPermission(final Permissions permission);

    Optional<RolePermissions> findByRoleAndPermission(final Roles role, final Permissions permission);
}
