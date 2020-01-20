package com.hangbokwatch.backend.dto.auth;

import com.hangbokwatch.backend.domain.user.Role;
import com.hangbokwatch.backend.domain.user.User;
import lombok.Builder;
import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Map;

@Getter
public class OAuthAttributes {
    private Map<String, Object> attributes;
    private String battleTag;
    private String email;
    private Long id;

    @Builder
    public OAuthAttributes(Map<String,Object> attributes, String battleTag, String email, Long id) {
        this.attributes = attributes;
        this.battleTag = battleTag;
        this.email = email;
        this.id = id;
    }

    // OAuth2User에서 반환하는 사용자 정보는 Map으로 되어있기 때문에 하나하나 변환해야한다. 그 역할을 한다.
    public static OAuthAttributes of(String registrationId, String id,
                                     Map<String, Object> attributes) {
        return ofBlizzard(id, attributes);
    }

    private static OAuthAttributes ofBlizzard(String id, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .id(Long.parseLong(id))
                .battleTag((String) attributes.get("battle_tag"))
                .email("")
                .attributes(attributes)
                .build();
    }

    // User 엔티티를 생성한다.
    // OAuthAttributes에서 엔티티를 생성하는 시점은 처음 가입할 때
    // 가입할 때의 기본 권한을 User로 준다.
    public User toEntitiy() {
        return User.builder()
                .id(id)
                .battleTag(battleTag)
                .email(email)
                .role(Role.USER)
                .lastLoginDtm(new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis()))
                .build();
    }
}
