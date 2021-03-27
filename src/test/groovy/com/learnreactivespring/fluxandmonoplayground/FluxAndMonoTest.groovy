package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import spock.lang.Specification

class FluxAndMonoTest extends Specification {
    def "Flux test"() {
        given:
        def stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
//            .concatWith(Flux.error(new RuntimeException("Oops")))
            .concatWith(Flux.just("After Error"))
            .log()

        stringFlux.subscribe(System.out.&println,
            { e -> System.err.println(e) },
            { println "Completed" })

        expect:
        true
    }

    def "Test flux elements without error"() {
        given:
        def stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNext("Spring")
            .expectNext("Spring Boot")
            .expectNext("Reactive Spring")
            .verifyComplete()
    }

    def "Test flux elements with error"() {
        given:
        def stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
            .concatWith(Flux.error(new RuntimeException("Oops")))
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNext("Spring")
            .expectNext("Spring Boot")
            .expectNext("Reactive Spring")
//            .expectError(RuntimeException)
            .expectErrorMessage("Oops")
            .verify()
    }

    def "Test count of flux elements"() {
        given:
        def stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNextCount(3)
            .verifyComplete()
    }

    def "Test flux with multiple nexts"() {
        given:
        def stringFlux = Flux.just("Spring", "Spring Boot", "Reactive Spring")
            .log()

        expect:
        StepVerifier.create(stringFlux)
            .expectNext("Spring", "Spring Boot", "Reactive Spring")
            .expectNext("Spring Boot")
            .expectNext("Reactive Spring")
            .verifyComplete()
    }

    def "Test Mono"() {
        given:
        def stringMono = Mono.just("Spring")

        expect:
        StepVerifier.create(stringMono.log())
            .expectNext("Spring")
            .verifyComplete()
    }

    def "Test Mono error"() {
        expect:
        StepVerifier.create(Mono.error(new RuntimeException("Oops")))
            .expectError(RuntimeException)
            .verify()
    }
}