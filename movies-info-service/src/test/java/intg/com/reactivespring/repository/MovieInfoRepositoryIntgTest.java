package com.reactivespring.repository;

import com.reactivespring.domain.MovieInfo;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@TestPropertySource(properties = "spring.mongodb.embedded.version=3.5.5")
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Autowired
    MovieInfoRepository movieInfoRepository;

    @BeforeEach
    void setUp() {
        var movieinfos = List.of(new MovieInfo(null, "Batman Begins",
                        2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15")),
                new MovieInfo(null, "The Dark Knight",
                        2008, List.of("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")),
                new MovieInfo("abc", "Dark Knight Rises",
                        2012, List.of("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")));

        movieInfoRepository.saveAll(movieinfos)
                .blockLast();
    }

    @AfterEach
    void tearDown() {
        movieInfoRepository.deleteAll().block();
    }

    @Test
    void findAll() {
        Flux<MovieInfo> movieInfoFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(3)
                .verifyComplete();

    }

    @Test
    void findById() {
        Mono<MovieInfo> movieInfoMono = movieInfoRepository.findById("abc").log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo -> {
                    assertEquals(movieInfo.getName(), "Dark Knight Rises");
                })
                .verifyComplete();

    }

    @Test
    void saveMovieInfo() {
        // given
        MovieInfo movieInfo = new MovieInfo(null, "Batman Begins1",
                2005, List.of("Christian Bale", "Michael Cane"), LocalDate.parse("2005-06-15"));

        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo1 -> {
                    assertEquals(movieInfo1.getName(), "Batman Begins1");
                })
                .verifyComplete();

    }

    @Test
    void updateMovieInfo() {
        // given
        MovieInfo movieInfo = movieInfoRepository.findById("abc").log().block();
        assert movieInfo != null;
        movieInfo.setYear(2021);

        Mono<MovieInfo> movieInfoMono = movieInfoRepository.save(movieInfo).log();

        StepVerifier.create(movieInfoMono)
                .assertNext(movieInfo1 -> {
                    assertEquals(movieInfo1.getYear(), 2021);
                })
                .verifyComplete();

    }

    @Test
    void deleteMovieInfo() {

        movieInfoRepository.deleteById("abc").block();

        Flux<MovieInfo> movieInfoFlux = movieInfoRepository.findAll().log();

        StepVerifier.create(movieInfoFlux)
                .expectNextCount(2)
                .verifyComplete();

    }
}