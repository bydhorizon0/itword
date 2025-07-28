package org.news.itword.security.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MemberAuthDTO;
import org.news.itword.entity.Member;
import org.news.itword.repository.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.stream.Collectors;

@Log4j2
@RequiredArgsConstructor
@Service
public class MemberDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        log.info("loadUserByUsername");

        Optional<Member> result = memberRepository.findByEmail(email);

        if (result.isEmpty()) {
            throw new UsernameNotFoundException("Check Email");
        }

        Member member = result.get();

        log.info("-------------------");
        log.info(member);

        MemberAuthDTO memberAuthDTO = new MemberAuthDTO(
                member.getId(),
                member.getEmail(),
                member.getPassword(),
                member.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toSet())
        );

        log.info("-----------------");
        log.info(memberAuthDTO);

        memberAuthDTO.setNickname(member.getNickname());

        return memberAuthDTO;
    }
}
