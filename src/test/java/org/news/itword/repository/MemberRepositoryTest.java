package org.news.itword.repository;

import org.junit.jupiter.api.Test;
import org.news.itword.entity.Member;
import org.news.itword.entity.MemberRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MemberRepositoryTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private MemberRepository memberRepository;

    @Test
    public void testInsertMembers() {
        IntStream.range(0, 50).forEach(i -> {
            Member member = Member.builder()
                    .email("user" + i + "@gmail.com")
                    .password(passwordEncoder.encode("123"))
                    .nickname("user" + i)
                    .build();

            member.addRole(MemberRole.USER);

            if (i > 40) {
                member.addRole(MemberRole.ADMIN);
            }
            memberRepository.save(member);
        });
    }
}