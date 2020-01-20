package com.hangbokwatch.backend.service.auth;

import com.hangbokwatch.backend.dao.user.UserRepository;
import com.hangbokwatch.backend.domain.user.User;
import com.hangbokwatch.backend.dto.auth.OAuthAttributes;
import com.hangbokwatch.backend.dto.auth.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.Collections;

@Slf4j
@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {
    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        log.info("OAuth >>>>>>>> loadUser | OAuth 진행중");
        OAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 현재 로그인 진행중인 서비스를 구분하는 코드. 현재는 블리자드만 사용하드로 불필요하지만 이후 다른 로그인 연동시 구분하기 위해 사용
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        log.info("OAuth >>>>>>>> loadUser | 현재 로그인 진행중인 서비스를 구분하는 코드. registrationId : {}", registrationId);
        //OAuth2 로그인 진행 시 키가 되는 필드값을 말한다.
        //구글의 경우 기본적으로 코드를 지원하지만 다른 사이트는 기본 지원하지 않는다.
        String userNameAttributeName = userRequest.getClientRegistration().getProviderDetails()
                                                    .getUserInfoEndpoint().getUserNameAttributeName();
        log.info("OAuth >>>>>>>> loadUser | OAuth2 로그인 진행 시 키가 되는 필드값을 말한다. userNameAttributeName : {}", userNameAttributeName);
        //OAuth2UserService를 통해 가져온 OAuth2User의 attribute를 담은 클래스
        //이후 네이버 등 다른 소셜 로그인도 이 클래스를 사용한다.
        log.info("OAuth >>>>>>>> loadUser | OAuth2 Attributes : {}", oAuth2User.getAttributes());
        OAuthAttributes attributes = OAuthAttributes.of(registrationId, userNameAttributeName, oAuth2User.getAttributes());
        log.info("OAuth >>>>>>>> loadUser | Custome OAuth2 Attributes : {}", attributes.getAttributes());
        User user = saveOrUpdate(attributes);
        // SessionUser : 세션 사용자 정보를 저장하기 위한 dto
        httpSession.setAttribute("user", new SessionUser(user));

        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRoleKey())),
                                       attributes.getAttributes(), userNameAttributeName);
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findByEmail(attributes.getEmail())
                .map(entity -> entity.update(attributes.getBattleTag(), attributes.getEmail(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())))
                .orElse(attributes.toEntitiy());

        return userRepository.save(user);
    }
}
