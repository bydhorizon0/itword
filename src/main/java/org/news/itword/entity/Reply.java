package org.news.itword.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString(exclude = {"member", "news", "parentReply", "childReplies"})
@Table(name = "tbl_replies")
public class Reply extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    private Reply parentReply;

    @Builder.Default
    @OneToMany(mappedBy = "parentReply", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reply> childReplies = new ArrayList<>();

}
