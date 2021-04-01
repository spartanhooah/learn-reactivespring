package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import spock.lang.Specification

import java.time.Duration

class FluxAndMonoWithTimeTest extends Specification {
    def "Infinite sequence"() {
        given:
        def infiniteFlux = Flux.interval(Duration.ofMillis(200)).log() // generates longs starting at 0

        expect:
        infiniteFlux.subscribe { println "Value is: $it" }
        Thread.sleep(3000)
    }
}
