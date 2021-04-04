package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.Duration

class VirtualTimeTest extends Specification {
    def "Without virtual time"() {
        given:
        def longFlux = Flux.interval(Duration.ofSeconds(1))
            .take(3)

        expect:
        StepVerifier.create(longFlux)
            .expectSubscription()
            .expectNext(0L, 1L, 2L)
            .verifyComplete()
    }

    def "With virtual time"() {
        expect:
        StepVerifier.withVirtualTime({
            Flux.interval(Duration.ofSeconds(1))
                .take(3).log()
        })
            .expectSubscription()
            .thenAwait(Duration.ofSeconds(3))
            .expectNext(0L, 1L, 2L)
            .verifyComplete()
    }
}
