package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.BaseSubscriber
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxAndMonoBackpressureTest extends Specification {
    def "Backpressure test"() {
        given:
        def finiteFlux = Flux.range(1, 10).log()

        expect:
        StepVerifier.create(finiteFlux)
            .expectSubscription()
            .thenRequest(1)
            .expectNext(1)
            .thenRequest(1)
            .expectNext(2)
            .thenCancel()
            .verify()
    }

    def "Backpressure"() {
        given:
        def finiteFlux = Flux.range(1, 10).log()

        expect:
        finiteFlux.subscribe(
            { println "Element is $it" },
            { println "Exception is $it" },
            { println "Done"},
            { it.request(2) })
    }

    def "Backpressure with cancel"() {
        given:
        def finiteFlux = Flux.range(1, 10).log()

        expect:
        finiteFlux.subscribe(
            { println "Element is $it" },
            { println "Exception is $it" },
            { println "Done"},
            { it.cancel() })
    }

    def "Customized backpressure"() {
        given:
        def finiteFlux = Flux.range(1, 10).log()

        expect:
        finiteFlux.subscribe(new BaseSubscriber<Integer>() {
            @Override
            protected void hookOnNext(Integer value) {
                request(1)
                println "Value received is $value"

                if (value == 4) {
                    cancel()
                }
            }
        })
    }
}
