package uy.kohesive.injekt

import org.junit.Test
import java.util.concurrent.atomic.AtomicInteger
import kotlin.properties.Delegates
import kotlin.test.assertEquals

public class TestGithub7 {
    @data class Singleton(val count: Int)

    companion object : InjektMain() {
        val counter = AtomicInteger()

        override fun InjektRegistrar.registerInjectables() {
            addSingletonFactory {
                Singleton(counter.incrementAndGet())
            }
        }
    }

    public val first: Singleton by Delegates.injectValue()
    public val second: Singleton by Delegates.injectValue()
    public val third: Singleton by Delegates.injectValue()

    @Test public fun testSingletonFactoryIsNotCalledMoreThanOnce() {
        assertEquals(1, counter.get())
        assertEquals(1, first.count)
        assertEquals(1, second.count)
        assertEquals(1, third.count)
    }
}
