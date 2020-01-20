package com.hangbokwatch.backend.config.auth;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;

public enum CustomOAuth2Provider {
    BLIZZARD {
        public ClientRegistration.Builder getBuilder(String registraionId) {
            ClientRegistration.Builder builder = getBuilder(registraionId,
                    ClientAuthenticationMethod.POST, DEFAULT_LOGIN_REDIRECT_URL)
                    .scope("openid")
                    .authorizationUri("https://kr.battle.net/oauth/authorize")
                    .tokenUri("https://kr.battle.net/oauth/token")
//                    .userInfoUri("https://kr.battle.net/oauth/userinfo")
                    .clientName("Blizzard");
//                    .userNameAttributeName("id_token");
            return builder;
        }
    };

    private static final String DEFAULT_LOGIN_REDIRECT_URL = "{baseUrl}/login/oauth2/code/{registrationId}";

    protected final ClientRegistration.Builder getBuilder(String registrationId, ClientAuthenticationMethod method, String redirectUri) {
        ClientRegistration.Builder builder = ClientRegistration.withRegistrationId(registrationId)
                                                                .clientAuthenticationMethod(method)
                                                                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                                                                .redirectUriTemplate(redirectUri);
        return builder;
    }

    public abstract ClientRegistration.Builder getBuilder(String registrationId);
}
