package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import static reactor.core.scheduler.Schedulers.parallel;

class FluxAndMonoTransformTest extends Specification {
    def names = ["Adam", "Anna", "Jack", "Jenny"]

    def "Transform using map"() {
        given:
        def namesFlux = Flux.fromIterable(names)
            .map { it.toUpperCase() }

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("ADAM", "ANNA", "JACK", "JENNY")
            .verifyComplete()
    }

    def "Transform using map and length"() {
        given:
        def namesFlux = Flux.fromIterable(names)
            .map { it.length() }

        expect:
        StepVerifier.create(namesFlux)
            .expectNext(4, 4, 4, 5)
            .verifyComplete()
    }

    def "Transform using map and length and repeat"() {
        given:
        def namesFlux = Flux.fromIterable(names)
            .map { it.length() }
            .repeat(1)
            .log()

        expect:
        StepVerifier.create(namesFlux)
            .expectNext(4, 4, 4, 5, 4, 4, 4, 5)
            .verifyComplete()
    }

    def "Transform using map and filter"() {
        given:
        def namesFlux = Flux.fromIterable(names)
            .filter { it.length() > 4 }
            .map { it.toUpperCase() }

        expect:
        StepVerifier.create(namesFlux)
            .expectNext("JENNY")
            .verifyComplete()
    }

    def "transform using flat map"() {
        given:
        def stringFlux = Flux.fromIterable(["A", "B", "C", "D", "E", "F"])
            .flatMap { Flux.fromIterable(convertToList(it)) } // need to make DB or external service call for each element (convertToList here); a flux is returned from that call
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNextCount(12)
            .verifyComplete()
    }

    def "transform using flat map in parallel"() {
        given:
        def stringFlux = Flux.fromIterable(["A", "B", "C", "D", "E", "F"])
            .window(6) // Creates Flux<Flux<String>> -> (A, B), (C, D), (E, F)
            .flatMap { it.map(this.&convertToList).subscribeOn(parallel()) } // <Flux<List<String>>
            .flatMap { Flux.fromIterable(it) } // <Flux<String>>
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNextCount(12)
            .verifyComplete()
    }

    def "transform using flat map in parallel but maintain order"() {
        given:
        def stringFlux = Flux.fromIterable(["A", "B", "C", "D", "E", "F"])
            .window(2) // Creates Flux<Flux<String>> -> (A, B), (C, D), (E, F)
            .flatMapSequential { it.map(this.&convertToList).subscribeOn(parallel()) } // <Flux<List<String>>
            .flatMap {Flux.fromIterable(it) } // <Flux<String>>
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNextCount(12)
            .verifyComplete()
    }

    def convertToList(string) {
        Thread.sleep(1000)

        Arrays.asList(string, "new value")
    }
}
