package com.hangbokwatch.backend.config.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.RequestEntity;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequestEntityConverter;
import org.springframework.util.MultiValueMap;

@Slf4j
public class CustomRequestEntityConverter implements Converter<OAuth2AuthorizationCodeGrantRequest, RequestEntity<?>> {
    private OAuth2AuthorizationCodeGrantRequestEntityConverter defaultConverter;

    public CustomRequestEntityConverter() {
        defaultConverter = new OAuth2AuthorizationCodeGrantRequestEntityConverter();
    }

    @Override
    public RequestEntity<?> convert(OAuth2AuthorizationCodeGrantRequest source) {
        RequestEntity<?> entity = defaultConverter.convert(source);
        MultiValueMap<String, String> params = (MultiValueMap<String, String>) entity.getBody();

        log.debug("OAuth Token Convert >>>>>>>> convert() | {} ", params);

        return new RequestEntity<>(params, entity.getHeaders(), entity.getMethod(), entity.getUrl());
    }
}
