package org.news.itword.repository;

import org.junit.jupiter.api.Test;
import org.news.itword.dto.ReplyDTO;
import org.news.itword.entity.Member;
import org.news.itword.entity.News;
import org.news.itword.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NewsRepository newsRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            long memberId = RandomGenerator.getDefault().nextLong(1, 50);
            long newsId = RandomGenerator.getDefault().nextLong(1, 100);

            Optional<Member> memberResult = memberRepository.findById(memberId);
            Optional<News> newsResult = newsRepository.findById(newsId);

            if (memberResult.isPresent() && newsResult.isPresent()) {
                Reply reply = Reply.builder()
                        .content("REPLY...." + i)
                        .news(newsResult.get())
                        .member(memberResult.get())
                        .build();

                replyRepository.save(reply);
            }
        });
    }

    @Test
    public void insertChildReplies() {
        List<Reply> allReplies = replyRepository.findAll();
        List<Member> allMembers = memberRepository.findAll();
        List<News> allNews = newsRepository.findAll();

        IntStream.rangeClosed(1, 300).forEach(i -> {
            Member member = allMembers.get(RandomGenerator.getDefault().nextInt(allMembers.size()));
            News news = allNews.get(RandomGenerator.getDefault().nextInt(allNews.size()));
            Reply parentReply = allReplies.get(RandomGenerator.getDefault().nextInt(allReplies.size()));

            Reply reply = Reply.builder()
                    .content("CHILD_REPLY...." + i)
                    .news(news)
                    .member(member)
                    .parentReply(parentReply)
                    .build();

            replyRepository.save(reply);
        });
    }

    @Test
    public void getParentReplies() {
        List<ReplyDTO> result = replyRepository.getParentReplies(1L);
        System.out.println(Objects.toString(result));
    }
}