package com.hangbokwatch.backend.dto.auth;

import com.hangbokwatch.backend.domain.user.Role;
import com.hangbokwatch.backend.domain.user.User;
import lombok.Getter;

import java.io.Serializable;


/**
 * User 클래스의 경우 엔티티로 설정되어 있기 떄문에 직렬화 기능을 가질 수 없다.
 * 따라서 직렬화 기능을 가진 세션 dto를 만들어 줄 필요가 있다.
 */
@Getter
public class SessionUser implements Serializable {
    private Long id;
    private String battleTag;
    private String email;
    private Integer trollIndex;
    private Integer badSpeakerIndex;
    private Role role;

    public SessionUser(User user) {
        this.battleTag = user.getBattleTag();
        this.id = user.getId();
        this.email = user.getEmail();
        this.trollIndex = user.getTrollIndex();
        this.badSpeakerIndex = user.getBadSpeakerIndex();
        this.role = user.getRole();
    }
}
