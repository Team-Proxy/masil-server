package com.beyond.masilbe.domain.iam.repository;

import com.beyond.masilbe.domain.iam.entity.Roles;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpaRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByRoleName(final String roleName);
}
