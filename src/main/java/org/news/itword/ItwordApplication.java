package org.news.itword;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ItwordApplication {

    public static void main(String[] args) {
        SpringApplication.run(ItwordApplication.class, args);
    }

}
