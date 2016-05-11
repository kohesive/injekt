package uy.kohesive.injekt.tests

import org.junit.Test
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.api.fullType
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals
import kotlin.test.fail

class TestGithub13 {

    // TODO: javaClass<T>() is deprecated so some of these paths should drop when we drop the Java Class accepting methods that are also deprecated here in Injekt

    open class Parser<T>(val name: String) {}
    class DescentParser(name: String): Parser<Array<Int>>(name)
    class DescentParserUnaliased(name: String): Parser<Array<Int>>(name)
    class DescentParserSetInt(name: String): Parser<Set<Int>>(name)
    class DescentParserSetLong(name: String): Parser<Set<Long>>(name)

    companion object : InjektMain() {
        val counter = AtomicInteger()

        override fun InjektScope.registerInjectables() {
            addSingleton(Parser<String>("one-string"))
            addSingleton(Parser<Int>("one-int"))
            addSingleton(Parser<Long>("one-long"))
            addSingleton<Parser<Double>>(Parser<Double>("one-double"))

            addSingleton(Parser<Array<String>>("two-string"))
            addSingleton(Parser<Array<Long>>("two-long"))

            addSingleton(DescentParser("two-int"))
            addAlias(fullType<DescentParser>(), fullType<Parser<Array<Int>>>())

            addSingleton(DescentParserUnaliased("two-int-noalias"))

            addSingleton(Parser<Map<String, List<String>>>("three-list-string"))
            addSingleton(Parser<Map<String, List<Int>>>("three-list-int"))
            addSingleton(Parser<Map<String, String>>("three-string"))

            val something = Parser<Map<String, Map<String, Any>>>("four-map-of-map-any")
            addSingleton(something)
            val somethingElse = Parser<Map<String, Map<String, String>>>("four-map-of-map-string")
            addSingleton(fullType<Parser<Map<String, Map<String, String>>>>(), somethingElse)

            addSingletonFactory { Parser<Set<String>>("five-string") }
            addSingletonFactory<Parser<Set<Int>>> { DescentParserSetInt("five-int") }
            addSingletonFactory(fullType<Parser<Set<Long>>>()) { DescentParserSetLong("five-long") }
        }
    }

    @Test
    fun testWithGenerics() {
        val oneString: Parser<String> = Injekt.get()
        assertEquals("one-string", oneString.name)
        assertEquals("one-string", Injekt.get<Parser<String>>().name)
        assertEquals("one-string", Injekt.get(fullType<Parser<String>>()).name)

        val oneStringAgain: Parser<String> = Injekt()
        assertEquals("one-string", oneStringAgain.name)

        assertEquals("one-int", Injekt.get<Parser<Int>>().name)
        assertEquals("one-double", Injekt.get<Parser<Double>>().name)

        val twoString: Parser<Array<String>> = Injekt.get()
        assertEquals("two-string", twoString.name)
        assertEquals("two-string", Injekt.get<Parser<Array<String>>>().name)
        assertEquals("two-long", Injekt.get<Parser<Array<Long>>>().name)
        assertEquals("two-int", Injekt.get<DescentParser>().name)
        assertEquals("two-int", Injekt.get<Parser<Array<Int>>>().name)
        assertEquals("two-int-noalias", Injekt.get<DescentParserUnaliased>().name)

        val threeString: Parser<Map<String, List<String>>> = Injekt.get()
        assertEquals("three-list-string", threeString.name)
        assertEquals("three-list-string", Injekt.get<Parser<Map<String, List<String>>>>().name)
        assertEquals("three-list-int", Injekt.get<Parser<Map<String, List<Int>>>>().name)
        assertEquals("three-string", Injekt.get<Parser<Map<String, String>>>().name)

        assertEquals("four-map-of-map-any", Injekt.get<Parser<Map<String, Map<String, Any>>>>().name)
        assertEquals("four-map-of-map-string", Injekt.get<Parser<Map<String, Map<String, String>>>>().name)

        assertEquals("five-string", Injekt.get<Parser<Set<String>>>().name)
    }

    @Test
    fun testDescendantsNotFound() {
        assertEquals("five-int", Injekt.get<Parser<Set<Int>>>().name)
        try {
            @Suppress("UNUSED_VARIABLE")
            val bad: DescentParserSetInt = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }

        assertEquals("five-long", Injekt.get<Parser<Set<Long>>>().name)
        try {
            @Suppress("UNUSED_VARIABLE")
            val bad: DescentParserSetLong = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }

    }

    @Test
    fun testDoesNotExistWithGenericType() {

        // doesn't exist at all
        try {
            @Suppress("UNUSED_VARIABLE")
            val bad: Parser<Array<BigDecimal>> = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }
    }
}
