package com.learnreactivespring.fluxandmonoplayground

import reactor.core.publisher.Flux
import spock.lang.Specification

import java.time.Duration

class HotAndColdPublisherTest extends Specification {
    def "Cold publisher test"() {
        given:
        def stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
            .delayElements(Duration.ofSeconds(1))
//            .log()

        expect:
        stringFlux.subscribe({println "Subscriber 1: $it"  }) // Gets values from beginning
        Thread.sleep(3000)

        stringFlux.subscribe({println "Subscriber 2: $it"  }) // Gets values from beginning
        Thread.sleep(4000)
    }

    def "Hot publisher test"() {
        given:
        def stringFlux = Flux.just("A", "B", "C", "D", "E", "F")
            .delayElements(Duration.ofSeconds(1))
//            .log()

        when:
        def connectableFlux = stringFlux.publish()
        connectableFlux.connect()

        then:
        connectableFlux.subscribe({println "Subscriber 1: $it"  })
        Thread.sleep(3000)

        connectableFlux.subscribe({println "Subscriber 2: $it"  }) // Doesn't get any values until it subscribes
        Thread.sleep(4000)
    }
}
