package com.reactivespring.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.test.web.reactive.server.expectBodyList
import reactor.test.StepVerifier

@WebFluxTest(controllers = [FluxAndMonoController::class])
@AutoConfigureWebTestClient
internal class FluxAndMonoControllerTest {

    @Autowired
    lateinit var webTestClient: WebTestClient

    @Test
    fun flux() {
        webTestClient.get().uri("/flux")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(Int::class.java)
            .hasSize(3)
    }

    @Test
    fun flux_approach2() {
        val flux = webTestClient.get().uri("/flux")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .returnResult(Int::class.java)
            .responseBody

        StepVerifier.create(flux)
            .expectNext(1,2,3)
            .verifyComplete()
    }

    @Test
    fun flux_approach3() {
        webTestClient.get().uri("/flux")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBodyList(Int::class.java)
            .consumeWith<WebTestClient.ListBodySpec<Int>> { entityExchangeResult ->
                val responseBody = entityExchangeResult.responseBody
                assert(responseBody!!.size == 3)
            }
    }

    @Test
    fun mono() {
        webTestClient.get().uri("/mono")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .expectBody(String::class.java)
            .consumeWith<WebTestClient.ListBodySpec<String>> { entityExchangeResult ->
                val responseBody = entityExchangeResult.responseBody
                assertEquals("Hello Pravin", responseBody)
            }
    }

    @Test
    fun stream() {
        val flux = webTestClient.get().uri("/stream")
            .exchange()
            .expectStatus()
            .is2xxSuccessful
            .returnResult(Long::class.java)
            .responseBody

        StepVerifier.create(flux)
            .expectNext(0L, 1L, 2L, 3L)
            .thenCancel()
            .verify()
    }
}