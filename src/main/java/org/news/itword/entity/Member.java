package org.news.itword.entity;


import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = "roles")
@Table(name = "tbl_members")
public class Member extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nickname;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "tbl_member_roles", joinColumns = @JoinColumn(name = "member_id"))
    @Enumerated(value = EnumType.ORDINAL)
    @Builder.Default
    private Set<MemberRole> roles = new HashSet<>();

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    public void addRole(MemberRole role) {
        roles.add(role);
    }
}
