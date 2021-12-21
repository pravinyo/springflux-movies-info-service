package com.reactivespring.controller

import com.reactivespring.domain.MovieInfo
import com.reactivespring.service.MoviesInfoService
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.web.reactive.server.WebTestClient
import reactor.core.publisher.Flux
import java.time.LocalDate

@WebFluxTest(controllers = [MoviesInfoController::class])
@AutoConfigureWebTestClient
class MoviesInfoControllerUnitTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockBean
    lateinit var moviesInfoServiceMock : MoviesInfoService

    companion object {
        const val MOVIES_INFO_URL = "/v1/movieinfos"
    }

    @Test
    fun getAllMoviesInfo() {
        `when`(moviesInfoServiceMock.getAllMovieInfos())
            .thenReturn(getDefaultList())

        webTestClient.get()
            .uri(MOVIES_INFO_URL)
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(MovieInfo::class.java)
            .hasSize(3)
    }

    private fun getDefaultList(): Flux<MovieInfo> {
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
        return Flux.fromIterable(moviesInfos)
    }
}