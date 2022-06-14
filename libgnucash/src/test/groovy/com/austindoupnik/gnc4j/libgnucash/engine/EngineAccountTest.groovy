package com.austindoupnik.gnc4j.libgnucash.engine


import spock.lang.Specification
import spock.lang.TempDir

import java.nio.file.Files
import java.nio.file.Path

import static EngineGncEngine.gnc_engine_init
import static EngineQofUtil.qof_close
import static EngineQofUtil.qof_init
import static com.austindoupnik.gnc4j.glib.GLibGList.toList
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineAccount.*
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineGncSession.gnc_get_current_session
import static com.austindoupnik.gnc4j.libgnucash.engine.EngineQofSession.*

class EngineAccountTest extends Specification {
    def setupSpec() {
        qof_init()
        gnc_engine_init(0, new String[]{null})
    }

    def cleanupSpec() {
        qof_close()
    }

    @TempDir
    Path tempDir

    def "should create file"() {
        when:
        def location = tempDir.resolve(UUID.randomUUID().toString())

        then:
        Files.notExists(location)

        when:
        def session = gnc_get_current_session()
        qof_session_begin(session, "xml://" + location, SessionOpenMode.SESSION_NEW_STORE)

        def book = qof_session_get_book(session)
        def rootAccount = gnc_book_get_root_account(book)

        def account0 = xaccMallocAccount(book)
        gnc_account_append_child(rootAccount, account0)
        xaccAccountBeginEdit(account0)
        xaccAccountSetType(account0, GNCAccountType.ACCT_TYPE_BANK)
        xaccAccountSetName(account0, "hello world")
        xaccAccountSetDescription(account0, "hello world")
        xaccAccountCommitEdit(account0)

        qof_session_save(session, null)
        qof_session_end(session)

        then:
        Files.exists(location)
    }

    def "confirm name account name violations"() {
        when:
        def location = tempDir.resolve(UUID.randomUUID().toString())

        then:
        Files.notExists(location)

        when:
        def session = gnc_get_current_session()
        qof_session_begin(session, "xml://" + location, SessionOpenMode.SESSION_NEW_STORE)

        def book = qof_session_get_book(session)
        def rootAccount = gnc_book_get_root_account(book)

        def accountNames = [
                "hello world",
                "jdifks",
                "good bye world",
        ]

        accountNames.forEach {
            def account0 = xaccMallocAccount(book)
            gnc_account_append_child(rootAccount, account0)
            xaccAccountBeginEdit(account0)
            xaccAccountSetType(account0, GNCAccountType.ACCT_TYPE_BANK)
            xaccAccountSetName(account0, it)
            xaccAccountCommitEdit(account0)
        }

        qof_session_save(session, null)
        qof_session_end(session)

        def violations = gnc_account_list_name_violations(book, "o")
        def result = toList(violations, p -> p.getString(0))

        then:
        result == ["good bye world", "hello world", "hello world"]
    }

    def "account string 'INCOME' converted to enum 'ACCT_TYPE_INCOME' by reference"() {
        when:
        def accountType = new GNCAccountType.ByReference()
        def successful = xaccAccountStringToType("INCOME", accountType)

        then:
        successful
        accountType.getValue() == GNCAccountType.ACCT_TYPE_INCOME
    }

    def "account string 'INCOME' converted to enum 'ACCT_TYPE_INCOME' by return"() {
        when:
        def accountType = xaccAccountStringToEnum("INCOME")

        then:
        accountType == GNCAccountType.ACCT_TYPE_INCOME
    }
}
