package com.reactivespring.controller

import com.reactivespring.domain.MovieInfo
import com.reactivespring.repository.MovieInfoRepository
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient
import java.time.LocalDate

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
internal class MoviesInfoControllerIntgTest {

    @Autowired
    lateinit var movieInfoRepository: MovieInfoRepository

    @Autowired
    lateinit var webTestClient: WebTestClient

    companion object {
        const val MOVIES_INFO_URL = "/v1/movieinfos"
    }

    @BeforeEach
    fun setup() {
        val moviesInfos = mutableListOf<MovieInfo>()
        moviesInfos.add(
            MovieInfo(null, "Batman Begins", 2005,
            listOf("Christian Bale", "Michael cane"), LocalDate.parse("2005-06-15"))
        )
        moviesInfos.add(
            MovieInfo(null, "The Dark Knight", 2008,
            listOf("Christian Bale", "HeathLedger"), LocalDate.parse("2008-07-18"))
        )
        moviesInfos.add(
            MovieInfo("abc", "Dark Knight Rises", 2012,
            listOf("Christian Bale", "Tom Hardy"), LocalDate.parse("2012-07-20"))
        )

        movieInfoRepository.saveAll(moviesInfos)
            .blockLast()
    }

    @AfterEach
    fun tearDown() {
        movieInfoRepository.deleteAll().block()
    }

    @Test
    fun addMovieInfo() {
        val movieInfo = MovieInfo(null, "Batman Begins1", 2005,
            listOf("Christian Bale", "Michael cane"), LocalDate.parse("2005-06-15"))

        webTestClient.post()
            .uri(MOVIES_INFO_URL)
            .bodyValue(movieInfo)
            .exchange()
            .expectStatus()
            .isCreated
            .expectBody(MovieInfo::class.java)
            .consumeWith<WebTestClient.BodySpec<MovieInfo, *>> {
                val savedMovieInfo = it.responseBody
                assertNotNull(savedMovieInfo)
                assertNotNull(savedMovieInfo?.movieInfoId)
            }
    }
}