package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxAndMonoFilterTest extends Specification {
    def names = ["Adam", "Anna", "Jack", "Jenny"]

    def "filter test"() {
        given:
        def namesFlux = Flux.fromIterable(names)
            .filter { it.startsWith("A") }
            .log()

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("Adam", "Anna")
            .verifyComplete()
    }

    def "filter test using length"() {
        given:
        def namesFlux = Flux.fromIterable(names)
            .filter { it.length() > 4 }
            .log()

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("Jenny")
            .verifyComplete()
    }
}
