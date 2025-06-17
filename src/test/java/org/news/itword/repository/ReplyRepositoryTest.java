package org.news.itword.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.news.itword.entity.Member;
import org.news.itword.entity.Movie;
import org.news.itword.entity.Reply;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

@SpringBootTest
class ReplyRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            long memberId = RandomGenerator.getDefault().nextLong(1, 51);
            long movieId = RandomGenerator.getDefault().nextLong(1, 100);

            Member member = memberRepository.findById(memberId).orElseThrow();
            Movie movie = movieRepository.findById(movieId).orElseThrow();

            Reply reply = Reply.builder()
                    .content("REPLY...." + i)
                    .member(member)
                    .movie(movie)
                    .build();

            replyRepository.save(reply);
        });
    }

    @Transactional
    @Rollback(value = false)
    @Test
    public void insertChildReplies() {
        List<Reply> allReplies = replyRepository.findAll();
        List<Member> allMembers = memberRepository.findAll();

        IntStream.rangeClosed(1, 300).forEach(i -> {
            Member member = allMembers.get(RandomGenerator.getDefault().nextInt(allMembers.size()));
            Reply parentReply = allReplies.get(RandomGenerator.getDefault().nextInt(allReplies.size()));
            Movie movie = parentReply.getMovie();

            Reply reply = Reply.builder()
                    .content("CHILD_REPLY...." + i)
                    .member(member)
                    .movie(movie)
                    .parentReply(parentReply)
                    .build();

            replyRepository.save(reply);
        });
    }

}