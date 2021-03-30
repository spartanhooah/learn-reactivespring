package com.learnreactivespring.fluxandmonoplayground

import reactor.core.Exceptions
import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import reactor.util.retry.Retry
import spock.lang.Specification

import java.time.Duration

class FluxAndMonoErrorTest extends Specification {
    def "Flux error handling on error resume"() {
        given:
        def stringFlux = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
            .concatWith(Flux.just("D"))
            .onErrorResume { e ->
                println "Exception is $e"
                Flux.just("default", "other default")
            }

        expect:
        StepVerifier.create(stringFlux.log())
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectNext("default", "other default")
            .verifyComplete()
    }

    def "Flux error handling on error return"() {
        given:
        def stringFlux = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
            .concatWith(Flux.just("D"))
            .onErrorReturn("default")

        expect:
        StepVerifier.create(stringFlux.log())
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectNext("default")
            .verifyComplete()
    }

    def "Flux error handling on error map"() {
        given:
        def stringFlux = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
            .concatWith(Flux.just("D"))
            .onErrorMap { e -> new CustomException(e) }

        expect:
        StepVerifier.create(stringFlux.log())
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectError(CustomException)
            .verify()
    }

    def "Flux error handling on error retry"() {
        given:
        def stringFlux = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
            .concatWith(Flux.just("D"))
            .onErrorMap { e -> new CustomException(e) }
            .retry(2)

        expect:
        StepVerifier.create(stringFlux.log())
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectNext("A", "B", "C")
            .expectNext("A", "B", "C")
            .expectError(CustomException)
            .verify()
    }

    def "Flux error handling on error map with retry backoff"() {
        given:
        def stringFlux = Flux.just("A", "B", "C")
            .concatWith(Flux.error(new RuntimeException("Exception Occurred")))
            .concatWith(Flux.just("D"))
            .onErrorMap { e -> new CustomException(e) }
            .retryWhen(Retry.backoff(2, Duration.ofSeconds(5)))

        expect:
        StepVerifier.create(stringFlux.log())
            .expectSubscription()
            .expectNext("A", "B", "C")
            .expectNext("A", "B", "C")
            .expectNext("A", "B", "C")
            .expectError(Exceptions.isRetryExhausted())
            .verify()
    }
}
