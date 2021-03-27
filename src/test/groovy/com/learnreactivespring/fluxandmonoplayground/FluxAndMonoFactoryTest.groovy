package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxAndMonoFactoryTest extends Specification {
    def names = ["Adam", "Anna", "Jack", "Jenny"]

    def "Flux using Iterable"() {
        given:
        def namesFlux = Flux.fromIterable(names)

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("Adam", "Anna", "Jack", "Jenny")
            .verifyComplete()
    }

    def "Flux using Array"() {
        given:
        String[] namesArray = names
        def namesFlux = Flux.fromArray(namesArray)

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("Adam", "Anna", "Jack", "Jenny")
            .verifyComplete()
    }

    def "Flux using Stream"() {
        given:
        def namesFlux = Flux.fromStream(names.stream())

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("Adam", "Anna", "Jack", "Jenny")
            .verifyComplete()
    }

    def "Mono using justOrEmpty"() {
        given:
        def mono = Mono.justOrEmpty(null)

        expect:
        StepVerifier.create(mono)
            .verifyComplete()
    }

    def "Mono using Supplier"() {
        given:
        def stringSupplier = { "Adam" }
        def stringMono = Mono.fromSupplier(stringSupplier)

        expect:
        StepVerifier.create(stringMono)
            .expectNext("Adam")
            .verifyComplete()
    }

    def "Mono using Range"() {
        given:
        def intFlux = Flux.range(1, 5)

        expect:
        StepVerifier.create(intFlux)
            .expectNext(1, 2, 3, 4, 5)
            .verifyComplete()k
    }
}
