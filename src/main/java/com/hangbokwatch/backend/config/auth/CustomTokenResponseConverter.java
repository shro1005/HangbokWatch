package com.hangbokwatch.backend.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse;
import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class CustomTokenResponseConverter implements Converter<Map<String, String>, OAuth2AccessTokenResponse> {

    @Override
    public OAuth2AccessTokenResponse convert(Map<String, String> source) {
        String accessToken = source.get(OAuth2ParameterNames.ACCESS_TOKEN);

        log.debug("OAuth Token Convert >>>>>>>> convert() | accessToken : {} ", accessToken);

        Set<String> scopes = Collections.emptySet();
        if (source.containsKey(OAuth2ParameterNames.SCOPE)) {
            String scope = source.get(OAuth2ParameterNames.SCOPE);
            scopes = Arrays.stream(StringUtils.delimitedListToStringArray(scope, ","))
                    .collect(Collectors.toSet());
        }
        long expiresIn = Long.valueOf(source.get(OAuth2ParameterNames.EXPIRES_IN));

        OAuth2AccessToken.TokenType accessTokenType = OAuth2AccessToken.TokenType.BEARER;

        return OAuth2AccessTokenResponse.withToken(accessToken)
                .tokenType(accessTokenType)
                .expiresIn(expiresIn)
                .scopes(scopes)
                .build();
    }
}
