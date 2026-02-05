package com.beyond.masilbe.domain.iam.repository;

import com.beyond.masilbe.domain.iam.entity.Permissions;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PermissionJpaRepository extends JpaRepository<Permissions, Long> {

    Optional<Permissions> findByPermissionName(final String permissionName);
}
