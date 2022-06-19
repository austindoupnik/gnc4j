package com.austindoupnik.gnc4j.libgnucash.engine

import com.sun.jna.JnaNativeString
import com.sun.jna.Memory
import jdk.nashorn.internal.objects.NativeString
import spock.lang.Specification

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncDate.*

class EngineGncDateTest extends Specification {
    def "should print today date"() {
        when:
        def today = gdate_to_time64(gnc_g_date_new_today().byValue())
        def actual = gnc_print_time64(today, "%Y-%m-%d")
        def expected = LocalDate.now().format(DateTimeFormatter.ofPattern("YYYY-MM-dd"))

        then:
        actual == expected
    }

    def "should convert to iso8601"() {
        when:
        def today = gdate_to_time64(gnc_g_date_new_today().byValue())

        def actual = new Memory(33)
        gnc_time64_to_iso8601_buff(today, actual)

        then:
        actual.getString(0) ==~ /\d{4}-\d{2}-\d{2} \d{2}:\d{2}:\d{2}/
    }
}
