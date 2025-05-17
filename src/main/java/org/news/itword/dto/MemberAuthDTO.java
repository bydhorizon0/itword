package org.news.itword.dto;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.time.LocalDateTime;
import java.util.Collection;

@Getter
@Setter
@ToString
public class MemberAuthDTO extends User {

    private Long id;
    private String nickname;
    private String email;
    private String password;
    private LocalDateTime createdAt, updatedAt;

    public MemberAuthDTO(String email, String password, Collection<? extends GrantedAuthority> authorities) {
        super(email, password, authorities);
        this.email = email;
        this.password = password;
    }

}
