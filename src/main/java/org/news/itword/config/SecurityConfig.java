package org.news.itword.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(CsrfConfigurer::disable)
                .authorizeHttpRequests(auth -> {
                    auth.requestMatchers("/css/**", "/js/**", "/images/**", "/lib/**", "/signup", "/login", "/logout").permitAll()
                            .anyRequest().authenticated();
                })
                .formLogin(form -> {
                    form.loginPage("/login")
                            .loginProcessingUrl("/login")
                            // alwaysUse에 true를 넣지 않으면 로그인 전의 요청 URL로 이동한다.
                            // 특정 페이지로만 강제 이동하고 싶을 때 사용
                            .defaultSuccessUrl("/movies", true)
                            .permitAll();
                })
                .logout(logout -> {
                    logout.logoutUrl("/logout")
                            .logoutSuccessUrl("/login")
                            .permitAll();
                })
                .build();
    }
}
