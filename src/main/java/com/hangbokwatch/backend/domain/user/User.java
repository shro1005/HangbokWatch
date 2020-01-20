package com.hangbokwatch.backend.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity(name="myuser")
public class User {
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "battle_tag")
    private String battleTag;

    @Enumerated(EnumType.STRING)
    @Column(name = "role")
    private Role role;

    @Column(name = "troll_index")
    private Integer trollIndex;

    @Column(name = "bad_speaker_index")
    private Integer badSpeakerIndex;

    @Column(name = "last_login_dtm")
    private String lastLoginDtm;

    @CreationTimestamp
    @Column(name="rgt_dtm", nullable = true)
    private LocalDateTime rgtDtm;

    @UpdateTimestamp
    @Column(name="udt_dtm", nullable = true)
    private LocalDateTime udtDtm;

    @Builder
    public User(Long id, String battleTag, String email, Role role, Integer trollIndex, Integer badSpeakerIndex, String lastLoginDtm) {
        this.id = id;
        this.battleTag = battleTag;
        this.email = email;
        this.role = role;
        this.trollIndex = trollIndex;
        this.badSpeakerIndex = badSpeakerIndex;
        this.lastLoginDtm = lastLoginDtm;
    }

    @Builder
    public User(Long id, String battleTag, String email, Role role, String lastLoginDtm) {
        this.id = id;
        this.battleTag = battleTag;
        this.email = email;
        this.role = role;
        this.lastLoginDtm = lastLoginDtm;
    }

    public User update(String battleTag, String email, String lastLoginDtm) {
        this.battleTag = battleTag;
        this.email = email;
        this.lastLoginDtm = lastLoginDtm;

        return this;
    }

    public User updateIndex(Integer trollIndex, Integer badSpeakerIndex) {
        this.trollIndex = trollIndex;
        this.badSpeakerIndex = badSpeakerIndex;

        return this;
    }

    public String getRoleKey() {
        return this.role.getKey();
    }
}
