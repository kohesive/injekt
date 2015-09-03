package uy.kohesive.injekt.tests

import org.junit.Test
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.*
import java.lang.reflect.Type
import java.math.BigDecimal
import java.util.concurrent.atomic.AtomicInteger
import kotlin.reflect.jvm.java
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.fail

public class TestGithub13 {

    open class Parser<T>(val name: String) {}
    class DescentParser(name: String): Parser<Array<Int>>(name)
    class DescentParserUnaliased(name: String): Parser<Array<Int>>(name)
    class DescentParserSetInt(name: String): Parser<Set<Int>>(name)
    class DescentParserSetLong(name: String): Parser<Set<Long>>(name)

    companion object : InjektMain() {
        val counter = AtomicInteger()

        override fun InjektRegistrar.registerInjectables() {
            Parser<String>("one-string").registerAsSingleton()
            Parser<Int>("one-int").registerAsSingleton()
            addSingleton(Parser<Long>("one-long"))
            addSingleton<Parser<Double>>(Parser<Double>("one-double"))

            Parser<Array<String>>("two-string").registerAsSingleton()
            addSingleton(Parser<Array<Long>>("two-long"))

            addSingleton(DescentParser("two-int"))
            addAlias(fullType<DescentParser>(), fullType<Parser<Array<Int>>>())

            addSingleton(DescentParserUnaliased("two-int-noalias"))

            Parser<Map<String, List<String>>>("three-list-string").registerAsSingleton()
            Parser<Map<String, List<Int>>>("three-list-int").registerAsSingleton()
            Parser<Map<String, String>>("three-string").registerAsSingleton()

            val something = Parser<Map<String, Map<String, Any>>>("four-map-of-map-any")
            something.registerAsSingleton()
            val somethingElse = Parser<Map<String, Map<String, String>>>("four-map-of-map-string")
            addSingleton(fullType<Parser<Map<String, Map<String, String>>>>(), somethingElse)

            addSingletonFactory { Parser<Set<String>>("five-string") }
            addSingletonFactory<Parser<Set<Int>>> { DescentParserSetInt("five-int") }
            addSingletonFactory(fullType<Parser<Set<Long>>>()) { DescentParserSetLong("five-long") }
        }
    }

    @Test
    public fun testWithGenerics() {
        val oneString: Parser<String> = Injekt.get()
        assertEquals("one-string", oneString.name)
        assertEquals("one-string", Injekt.get<Parser<String>>().name)
        assertEquals("one-string", Injekt.getInstance(javaClass<Parser<String>>()).name)
        assertEquals("one-string", Injekt.getInstance(fullType<Parser<String>>()).name)

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
    public fun testDescendantsNotFound() {
        assertEquals("five-int", Injekt.get<Parser<Set<Int>>>().name)
        try {
            val bad: DescentParserSetInt = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }

        assertEquals("five-long", Injekt.get<Parser<Set<Long>>>().name)
        try {
            val bad: DescentParserSetLong = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }

    }

    @Test
    public fun testDoesNotExistWithGenericType() {

        // doesn't exist at all
        try {
            val bad: Parser<Array<BigDecimal>> = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }
    }

    data class SomethingErased1<T>(val thing: T)

    @Test
    public fun testUsingErasedClasses() {
        Companion.scope.addSingleton(SomethingErased1::class.java, SomethingErased1("Hi"))

        // doesn't exist at all
        try {
            val bad: SomethingErased1<String> = Injekt.get()
            fail("Expecting a no factory exception")
        } catch (ex: Throwable) {
            // pass
        }

        assertEquals("Hi", Injekt.get(SomethingErased1::class.java).thing)
    }
}
