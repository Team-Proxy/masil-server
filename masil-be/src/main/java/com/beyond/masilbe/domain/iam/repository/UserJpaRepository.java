package com.beyond.masilbe.domain.iam.repository;

import com.beyond.masilbe.domain.iam.entity.Users;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpaRepository extends JpaRepository<Users, Long> {

    // 이메일
    Optional<Users> findByEmail(final String email);
}
