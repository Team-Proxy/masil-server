package com.beyond.masilbe.domain.iam.entity;

import com.beyond.masilbe.common.entity.BaseEntity;
import com.beyond.masilbe.domain.iam.dto.user.request.CreateUserRequestDto;
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
        name = "users",
        indexes = {@Index(name = "idx_user_dpt_id", columnList = "dpt_id")})
@AttributeOverride(name = "id", column = @Column(name = "user_id"))
@SQLRestriction("deleted_at IS NULL")
public class Users extends BaseEntity {

    @Column(name = "user_name", length = 20, nullable = false)
    private String userName;

    @Column(name = "nickname", length = 20, nullable = false)
    private String nickname;

    @Column(name = "email", length = 30, nullable = false) // UNIQUE
    private String email;

    @Column(name = "password", length = 255, nullable = false)
    private String password;

    public static Users create(final CreateUserRequestDto request, final String encryptedPassword) {
        return Users.builder()
                .userName(request.getUserName())
                .nickname(request.getNickname())
                .email(request.getEmail())
                .password(encryptedPassword)
                .build();
    }
}
