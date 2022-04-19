package com.austindoupnik.gnc4j.glib


import spock.lang.Specification

import static GLibGList.*
import static com.sun.jna.JnaNativeString.asPointer

class GLibGListTest extends Specification {
    def "fromList and toList preserve contents"() {
        given:
        def expected = ["abc", "xyz", "def"]

        when:
        def gList = fromList(expected, s -> asPointer(s))
        def actual = toList(gList, p -> p.getString(0))

        then:
        expected == actual

        cleanup:
        g_list_free(gList)
    }

    def "length of list after fromList matches original"() {
        given:
        def elements = ["a", "b", "c", "d", "e"]
        def expected = elements.size()

        when:
        def gList = fromList(elements, s -> asPointer(s))
        def actual = g_list_length(gList).intValue()

        then:
        actual == expected

        cleanup:
        g_list_free(gList)
    }
}
