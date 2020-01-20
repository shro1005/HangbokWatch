package com.hangbokwatch.backend.config.auth;

import com.hangbokwatch.backend.domain.user.Role;
import com.hangbokwatch.backend.service.auth.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    private final CustomOAuth2UserService customOAuth2UserService;

    /**
     * WebSecurityConfigurerAdapter을 상속받아 요청을 가로챈다.
     * @param http
     * @throws Exception
     */
    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable().headers().frameOptions().disable() // h2-console 화면을 사용하기 위한 설정
                .and().authorizeRequests() // 메소드를 통해 여러 자식을 추가하여 URL에 대한 요구사항을 정의할 수 있다 (antMatch를 사용할 수 있다.).
                    .antMatchers("/**", "/login/** ","/css/**", "/images/**", "/js/**", "/font/**", "/scss/**").permitAll()
                    .antMatchers("/findDuo/**").hasRole(Role.USER.name()).anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/")

                .and().oauth2Login().successHandler(successHandler())
//                    .tokenEndpoint().accessTokenResponseClient(accessTokenResponseClient()).and()
                // oauth2Login : Oauth2 로그인 기능에 대한 설정 진입점
                // userInfoEndpoint : Oauth2 로그인 성공 후 사용자 정보를 가져올때 설정 담당
                // userService : 소셜 로그인 성공 시 후속 조치를 진행할 UserService 인터페이스 구현체 등록
                    .userInfoEndpoint().userService(customOAuth2UserService);
    }

    @Bean
    public AuthenticationSuccessHandler successHandler() {
        return new CustomLoginSuccessHandler("/");
    }

    /**
     * OAuth2 기본 프로세스는 Access Token을 통해 정보를 조회하는 형태의  교환하는 방식이다. 이러한 방식을 사용하면 Access Token 발급이후
     * 필요한 정보를 Access Token 을 담아 다시 전송해야한다.
     * 하지만 JWS (JSON Web Signature)방식의 경우 JWT (JSON Web Token)을 받는다. 기존 Access Token과는 달리 JWT의 경우
     * Token에 필요한 데이터가 함께 들어가 있기 때문에 따로 요청을 보내서 필요한 데이터를 받을 필요가 없어 오버헤드를 방지할 수 있다.
     */

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository(
            @Value("${spring.security.oauth2.client.registration.blizzard.client-id}") String blizzardClientId,
            @Value("${spring.security.oauth2.client.registration.blizzard.client-secret}") String blizzardClientSecret
    ) {
        List<ClientRegistration> registrations = new ArrayList<>();
        registrations.add(CustomOAuth2Provider.BLIZZARD.getBuilder("blizzard")
                        .clientId(blizzardClientId)
                        .clientSecret(blizzardClientSecret)
                        .jwkSetUri("https://kr.battle.net/oauth/jwks/certs")
                        .build());

        return new InMemoryClientRegistrationRepository(registrations);
    }

    /**
     * AccessToken 을 받아 응답하는 클라이언트를 직접 구현할떄 사용
     *
     */
//    @Bean
//    public OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
//        DefaultAuthorizationCodeTokenResponseClient accessTokenResponseClient =
//                new DefaultAuthorizationCodeTokenResponseClient();
//        accessTokenResponseClient.setRequestEntityConverter(new CustomRequestEntityConverter());
//
//        OAuth2AccessTokenResponseHttpMessageConverter tokenResponseHttpMessageConverter =
//                new OAuth2AccessTokenResponseHttpMessageConverter();
//        tokenResponseHttpMessageConverter.setTokenResponseConverter(new CustomTokenResponseConverter());
//        RestTemplate restTemplate = new RestTemplate(Arrays.asList(
//                new FormHttpMessageConverter(), tokenResponseHttpMessageConverter
//        ));
//
//        accessTokenResponseClient.setRestOperations(restTemplate);
//        return accessTokenResponseClient;
//    }
}
