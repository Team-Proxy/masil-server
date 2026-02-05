package com.beyond.masilbe.domain.iam.repository;

import com.beyond.masilbe.domain.iam.entity.Roles;
import com.beyond.masilbe.domain.iam.entity.UserRoles;
import com.beyond.masilbe.domain.iam.entity.Users;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRoleJpaRepository extends JpaRepository<UserRoles, Long> {

    List<UserRoles> findAllByUser(final Users user);

    List<UserRoles> findAllByRole(final Roles role);

    Optional<UserRoles> findByUserAndRole(final Users user, final Roles role);
}
