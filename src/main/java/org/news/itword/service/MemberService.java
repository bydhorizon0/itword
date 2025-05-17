package org.news.itword.service;

import org.news.itword.dto.MemberAuthDTO;
import org.news.itword.dto.MemberDTO;
import org.news.itword.entity.Member;

public interface MemberService {

    Long join(MemberDTO dto);
    MemberAuthDTO getById(Long id);
    MemberAuthDTO getByEmail(String email);
    void modify(MemberAuthDTO dto);
    void remove(Long id, String email);

    default Member dtoToEntity(MemberDTO dto) {
        return Member.builder()
                .id(dto.getId())
                .nickname(dto.getNickname())
                .email(dto.getEmail())
                .password(dto.getPassword())
                .build();
    }

}
