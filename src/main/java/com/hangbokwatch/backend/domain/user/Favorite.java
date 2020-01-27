package com.hangbokwatch.backend.domain.user;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.time.LocalDateTime;

@Getter
@Entity(name = "favorite")
@IdClass(FavoriteId.class)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Favorite {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;

    @Id
    @Column(name = "clicked_id", nullable = false)
    private Long clickedId;

    @Column(name = "like_or_not")
    private String likeornot;

    @CreationTimestamp
    @Column(name="rgt_dtm", nullable = true)
    private LocalDateTime rgtDtm;

    @UpdateTimestamp
    @Column(name="udt_dtm", nullable = true)
    private LocalDateTime udtDtm;

    public Favorite(Long id, Long clickedId, String likeOrNot) {
        this.id = id;
        this.clickedId = clickedId;
        this.likeornot = likeOrNot;
    }

}
