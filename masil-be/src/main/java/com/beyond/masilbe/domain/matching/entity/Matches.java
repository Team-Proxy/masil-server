package com.beyond.masilbe.domain.matching.entity;

import static com.beyond.masilbe.domain.matching.enums.MatchStatus.*;

import com.beyond.masilbe.common.entity.BaseEntity;
import com.beyond.masilbe.domain.matching.enums.MatchStatus;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Getter
@Entity
@Table(name = "matches")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AttributeOverride(name = "id", column = @Column(name = "match_id"))
@SQLRestriction("deleted_at IS NULL")
public class Matches extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private MatchStatus status;

    @Column(name = "requester_id", nullable = false)
    private Long requesterId;

    @Column(name = "receiver_id", nullable = false)
    private Long receiverId;

    @Builder
    private Matches(Long requesterId, Long receiverId, MatchStatus status) {
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.status = status;
    }

    public void accept() {
        this.status = ACCEPTED;
    }

    public void reject() {
        this.status = REJECTED;
    }

    public void cancel() {
        this.status = CANCELED;
    }
}
