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
}