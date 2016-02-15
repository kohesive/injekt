package uy.kohesive.injekt.tests

import org.junit.Test
import uy.kohesive.injekt.api.*
import uy.kohesive.injekt.*
import java.util.concurrent.atomic.AtomicInteger
import kotlin.test.assertEquals

class TestGithub7 {
    data class Singleton(val count: Int)

    companion object : InjektMain() {
        val counter = AtomicInteger()

        override fun InjektScope.registerInjectables() {
            addSingletonFactory {
                Singleton(counter.incrementAndGet())
            }
        }
    }

    val first: Singleton by injectValue()
    val second: Singleton by injectValue()
    val third: Singleton by injectValue()

    @Test fun testSingletonFactoryIsNotCalledMoreThanOnce() {
        assertEquals(1, counter.get())
        assertEquals(1, first.count)
        assertEquals(1, second.count)
        assertEquals(1, third.count)
    }
}
