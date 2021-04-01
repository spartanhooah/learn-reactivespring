package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.Duration

class FluxAndMonoWithTimeTest extends Specification {
    def "Infinite sequence"() {
        given:
        def infiniteFlux = Flux.interval(Duration.ofMillis(100)).log() // generates longs starting at 0

        expect:
        infiniteFlux.subscribe { println "Value is: $it" }
        Thread.sleep(3000)
    }

    def "Infinite sequence test"() {
        given:
        def finiteFlux = Flux.interval(Duration.ofMillis(100))
            .take(3)
            .log()

        expect:
        StepVerifier.create(finiteFlux)
            .expectSubscription()
            .expectNext(0L, 1L, 2L)
            .verifyComplete()
    }

    def "Infinite sequence map"() {
        given:
        def finiteFlux = Flux.interval(Duration.ofMillis(100))
            .map { new Integer(it.intValue()) }
            .take(3)
            .log()

        expect:
        StepVerifier.create(finiteFlux)
            .expectSubscription()
            .expectNext(0, 1, 2)
            .verifyComplete()
    }

    def "Infinite sequence map with delay"() {
        given:
        def finiteFlux = Flux.interval(Duration.ofMillis(200))
            .delayElements(Duration.ofSeconds(1))
            .map { new Integer(it.intValue()) }
            .take(3)
            .log()

        expect:
        StepVerifier.create(finiteFlux)
            .expectSubscription()
            .expectNext(0, 1, 2)
            .verifyComplete()
    }
}
