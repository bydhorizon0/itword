package org.news.itword.repository;

import org.junit.jupiter.api.Test;
import org.news.itword.dto.NewsDTO;
import org.news.itword.entity.Member;
import org.news.itword.entity.News;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.random.RandomGenerator;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NewsRepositoryTest {

    @Autowired
    private NewsRepository newsRepository;

    @Test
    public void insertDummies() {
        IntStream.rangeClosed(1, 100).forEach(i -> {
            long id = RandomGenerator.getDefault().nextLong(1, 50);
            Member member = Member.builder().id(id).build();

            News news = News.builder()
                    .title("NEWS_TITLE..." + i)
                    .content("CONTENT..." + i)
                    .member(member)
                    .build();

            newsRepository.save(news);
        });
    }

    @Test
    public void getNewsList() {
        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("id").descending());

        Page<NewsDTO> newsList = newsRepository.getNewsList(pageRequest);
        newsList.get().forEach(System.out::println);
    }

    @Test
    public void getNewsInfo() {
        newsRepository.getNewsInfo(1L);
    }

}