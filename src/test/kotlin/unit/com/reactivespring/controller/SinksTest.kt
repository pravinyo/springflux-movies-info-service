package com.reactivespring.controller

import org.junit.jupiter.api.Test
import reactor.core.publisher.Sinks

class SinksTest {

    @Test
    fun sink() {
        val replaySink = Sinks.many().replay().all<Int>()

        replaySink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)
        replaySink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)
        replaySink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST)

        val fluxInteger1 = replaySink.asFlux()
        fluxInteger1.subscribe { println("Subscriber 1 Event: $it") }

        val fluxInteger2 = replaySink.asFlux()
        fluxInteger2.subscribe { println("Subscriber 2 Event: $it") }

        replaySink.tryEmitNext(4)
    }

    @Test
    fun sinks_multicast() {
        val multicastSink = Sinks.many().multicast().onBackpressureBuffer<Int>()

        multicastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)
        multicastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)
        multicastSink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST)

        val fluxInteger1 = multicastSink.asFlux()
        fluxInteger1.subscribe { println("Subscriber 1 Event: $it") }

        val fluxInteger2 = multicastSink.asFlux()
        fluxInteger2.subscribe { println("Subscriber 2 Event: $it") }

        multicastSink.emitNext(4, Sinks.EmitFailureHandler.FAIL_FAST)
    }

    @Test
    fun sinks_unicast() {
        val unicastSink = Sinks.many().unicast().onBackpressureBuffer<Int>()

        unicastSink.emitNext(1, Sinks.EmitFailureHandler.FAIL_FAST)
        unicastSink.emitNext(2, Sinks.EmitFailureHandler.FAIL_FAST)
        unicastSink.emitNext(3, Sinks.EmitFailureHandler.FAIL_FAST)

        val fluxInteger1 = unicastSink.asFlux()
        fluxInteger1.subscribe { println("Subscriber 1 Event: $it") }

        val fluxInteger2 = unicastSink.asFlux()
        fluxInteger2.subscribe { println("Subscriber 2 Event: $it") }

        unicastSink.emitNext(4, Sinks.EmitFailureHandler.FAIL_FAST)
    }
}