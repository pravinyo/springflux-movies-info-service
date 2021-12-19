package com.reactivespring.repository

import com.reactivespring.domain.MovieInfo
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest
import org.springframework.test.context.ActiveProfiles
import reactor.test.StepVerifier
import java.time.LocalDate

@DataMongoTest
@ActiveProfiles("test")
class MovieInfoRepositoryIntgTest {

    @Autowired
    lateinit var movieInfoRepository: MovieInfoRepository

    @BeforeEach
    fun setup() {
        val moviesInfos = mutableListOf<MovieInfo>()
        moviesInfos.add(MovieInfo(null, "Batman Begins", 2005,
            listOf("Christian Bale", "Michael cane"), LocalDate.parse("2005-06-15")))
        moviesInfos.add(MovieInfo(null, "The Dark Knight", 2008,
            listOf("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18")))
        moviesInfos.add(MovieInfo("abc", "Dark Knight Rises", 2012,
            listOf("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20")))

        movieInfoRepository.saveAll(moviesInfos)
            .blockLast()
    }

    @AfterEach
    fun tearDown() {
        movieInfoRepository.deleteAll().block()
    }

    @Test
    fun findAll() {

        val moviesInfoFlux = movieInfoRepository.findAll().log()

        StepVerifier.create(moviesInfoFlux)
            .expectNextCount(3)
            .verifyComplete()
    }

    @Test
    fun findById() {

        val moviesInfoMono = movieInfoRepository.findById("abc").log()

        StepVerifier.create(moviesInfoMono)
            .assertNext { movieInfo ->
                assertEquals("Dark Knight Rises", movieInfo.name)
            }.verifyComplete()
    }
}