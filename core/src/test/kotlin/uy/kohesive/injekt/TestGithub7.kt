package uy.kohesive.injekt.tests

import org.junit.Test
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.injectValue
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates
import kotlin.test.assertEquals

public class TestGithub7 {
    data class Singleton(val count: Int)

    companion object : InjektMain() {
        val counter = AtomicInteger()

        override fun InjektRegistrar.registerInjectables() {
            addSingletonFactory {
                Singleton(counter.incrementAndGet())
            }
        }
    }

    public val first: Singleton by injectValue()
    public val second: Singleton by injectValue()
    public val third: Singleton by injectValue()

    @Test public fun testSingletonFactoryIsNotCalledMoreThanOnce() {
        assertEquals(1, counter.get())
        assertEquals(1, first.count)
        assertEquals(1, second.count)
        assertEquals(1, third.count)
    }
}
