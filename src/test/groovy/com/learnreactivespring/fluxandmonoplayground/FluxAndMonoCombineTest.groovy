package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import reactor.test.StepVerifier
import spock.lang.Specification

import java.time.Duration
import java.util.function.BiFunction

class FluxAndMonoCombineTest extends Specification {
    static final def ONE_FLUX = Flux.just("A", "B", "C")
    static final def OTHER_FLUX = Flux.just("D", "E", "F")
    static final def MERGED_FLUX = Flux.merge(ONE_FLUX, OTHER_FLUX)
    static final def ONE_FLUX_DELAYED = Flux.just("A", "B", "C").delayElements(Duration.ofSeconds(1))
    static final def OTHER_FLUX_DELAYED = Flux.just("D", "E", "F").delayElements(Duration.ofSeconds(1))

    def "combine using merge"() {
        expect:
        StepVerifier.create(MERGED_FLUX)
            .expectSubscription()
            .expectNext("A", "B", "C", "D", "E", "F")
            .verifyComplete()
    }

    def "combine using merge with delay"() {
        expect:
        StepVerifier.create(Flux.merge(ONE_FLUX_DELAYED, OTHER_FLUX_DELAYED).log())
            .expectSubscription()
            .expectNextCount(6)
            .verifyComplete()
    }
    
    def "combine using concat"() {
        expect:
        StepVerifier.create(Flux.concat(ONE_FLUX, OTHER_FLUX).log())
            .expectSubscription()
            .expectNextCount(6)
            .verifyComplete()
    }

    def "combine using concat with delay"() {
        expect:
        StepVerifier.create(Flux.concat(ONE_FLUX_DELAYED, OTHER_FLUX_DELAYED).log())
            .expectSubscription()
            .expectNextCount(6)
            .verifyComplete()
    }

    def "combine using zip"() {
        expect:
        StepVerifier.create(Flux.zip(ONE_FLUX, OTHER_FLUX, { t1, t2 -> t1.concat(t2) } as BiFunction).log())
            .expectSubscription()
            .expectNext("AD", "BE", "CF")
            .verifyComplete()
    }
}
