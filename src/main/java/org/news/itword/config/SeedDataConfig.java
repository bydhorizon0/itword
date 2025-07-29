package org.news.itword.config;

import lombok.RequiredArgsConstructor;
import org.news.itword.repository.MovieRepository;
import org.news.itword.service.SeedService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class SeedDataConfig {

    private final SeedService seedService;

    @Bean
    public CommandLineRunner commandLineRunner(MovieRepository movieRepository) {
        return args -> {
            System.out.println("movies 테이블 영화 수 확인...");
            long movieCount = movieRepository.count();
            System.out.println("현재 영화 개수: " + movieCount);

            if (movieCount == 0) {
                System.out.println("데이터 시딩 시작");
                try {
                    seedService.scrap();
                } catch (Exception e) {
                    System.out.println("데이터 시딩 실패: " + e.getMessage());
                }
            } else {
                System.out.println("이미 데이터가 존재하므로 시딩 건너뜀");
            }
        };
    }

}