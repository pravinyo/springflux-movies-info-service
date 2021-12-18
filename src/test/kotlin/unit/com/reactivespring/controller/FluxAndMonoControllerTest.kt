package com.reactivespring.controller

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.test.web.reactive.server.WebTestClient

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
            .expectBodyList(Integer::class.java)
            .hasSize(3)
    }
}