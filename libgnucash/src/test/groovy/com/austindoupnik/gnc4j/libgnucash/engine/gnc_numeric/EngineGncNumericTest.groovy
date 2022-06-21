package com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric

import com.austindoupnik.gnc4j.glib.GInt
import com.austindoupnik.gnc4j.glib.GInt64
import spock.lang.Specification

import static com.austindoupnik.gnc4j.libgnucash.engine.gnc_numeric.EngineGncNumeric.*

class EngineGncNumericTest extends Specification {
    def "should add"() {
        when:
        def a = new gnc_numeric()
        string_to_gnc_numeric("2.0", a)

        def b = new gnc_numeric()
        string_to_gnc_numeric("3.0", b)

        def result = gnc_numeric_add(
                a.byValue(),
                b.byValue(),
                new GInt64(10),
                new GInt(GNCHowDenom.GNC_HOW_DENOM_EXACT.getValue())
        )

        then:
        gnc_numeric_to_string(result.byValue()) == "50/10"
    }
}
