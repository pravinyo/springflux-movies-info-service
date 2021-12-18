package com.reactivespring.controller

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux

@RestController
class FluxAndMonoController {

    @GetMapping("/flux")
    fun flux() : Flux<Int> {

        return Flux.just(1,2,3).log()
    }
}