package org.news.itword.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.news.itword.dto.MemberAuthDTO;
import org.news.itword.dto.MemberDTO;
import org.news.itword.entity.Member;
import org.news.itword.repository.MemberRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Log4j2
@RequiredArgsConstructor
@Service
public class MemberServiceImpl implements MemberService {

    private final PasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    @Override
    public Long join(MemberDTO dto) {
        log.info("MemberServiceImpl::join...{}", dto);

        dto.setPassword(passwordEncoder.encode(dto.getPassword()));

        Member save = memberRepository.save(dtoToEntity(dto));
        return save.getId();
    }

    @Override
    public MemberAuthDTO getById(Long id) {
        return null;
    }

    @Override
    public MemberAuthDTO getByEmail(String email) {
        return null;
    }

    @Override
    public void modify(MemberAuthDTO dto) {

    }

    @Override
    public void remove(Long id, String email) {

    }
}
