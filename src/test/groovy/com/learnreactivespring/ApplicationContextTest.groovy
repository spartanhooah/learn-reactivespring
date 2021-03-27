package com.learnreactivespring

import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

@SpringBootTest
class ApplicationContextTest extends Specification {
    def "The application context loads successfully"() {
        expect:
        true
    }
}
