package com.hangbokwatch.backend.config.auth;

import com.hangbokwatch.backend.dao.user.UserRepository;
import com.hangbokwatch.backend.domain.user.User;
import com.hangbokwatch.backend.dto.auth.OAuthAttributes;
import com.hangbokwatch.backend.dto.auth.SessionUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2LoginAuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Map;

@Slf4j
public class CustomLoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
    @Autowired
    UserRepository userRepository;

    public CustomLoginSuccessHandler(String defaultTargetUrl) {
        setDefaultTargetUrl(defaultTargetUrl);
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws ServletException, IOException {

        OAuth2AuthenticationToken authenticationResult = (OAuth2AuthenticationToken) authentication;
        String registrationId = authenticationResult.getAuthorizedClientRegistrationId();

        log.debug("{} onAuthenticationSuccess >>>>>>>> jwt : {}", registrationId, authentication.getPrincipal());
        DefaultOidcUser responseUser = ((DefaultOidcUser)authentication.getPrincipal());
        String id = responseUser.getName();
        Map<String, Object> attributes = responseUser.getAttributes();
        String battleTag = (String) attributes.get("battle_tag");
        log.debug("{} onAuthenticationSuccess >>>>>>>> Data Parsing | user id : {} / battle-tag : {}", registrationId, id, battleTag);
        OAuthAttributes customAttributes = OAuthAttributes.of(registrationId, id, attributes);
        User user = saveOrUpdate(customAttributes);

        HttpSession session = request.getSession();
        session.setAttribute("user", new SessionUser(user));

        if (session != null) {
            String redirectUrl = (String) session.getAttribute("prevPage");
            if (redirectUrl != null) {
                session.removeAttribute("prevPage");
                getRedirectStrategy().sendRedirect(request, response, redirectUrl);
            } else {
                super.onAuthenticationSuccess(request, response, authentication);
            }
        } else {
            super.onAuthenticationSuccess(request, response, authentication);
        }
    }

    private User saveOrUpdate(OAuthAttributes attributes) {
        User user = userRepository.findById(attributes.getId())
                .map(entity -> entity.update(attributes.getBattleTag(), attributes.getEmail(), new SimpleDateFormat("yyyyMMddHHmmss").format(System.currentTimeMillis())))
                .orElse(attributes.toEntitiy());

        return userRepository.save(user);
    }
}
