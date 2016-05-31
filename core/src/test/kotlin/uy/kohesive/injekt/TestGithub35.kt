package uy.kohesive.injekt

import org.junit.Before
import org.junit.Test
import uy.kohesive.injekt.registry.default.DefaultRegistrar
import kotlin.test.assertEquals

class TestGuthub35 {

    @Before fun prepareTest() {
        Injekt.addSingletonFactory {
            val function: (value: Int) -> Int = { value -> value + 1 }
            function
        }

        Injekt.addSingletonFactory {
            val function: (Long) -> Long = { value -> value - 1 }
            function
        }

        Injekt.addSingletonFactory {
            val function: (Long) -> String = { value -> "The long is $value" }
            function
        }

        Injekt.addSingletonFactory {
            val function: (Int) -> Int = { value -> value + 20 }
            Int1Action(function)
        }
    }

    @Test fun testInjectFunctionType() {
        val intFunction: (Int) -> Int = Injekt.get()
        assertEquals(3, intFunction(2))

        val longFunction: (Long) -> Long = Injekt.get()
        assertEquals(1, longFunction(2))

        val longStringFunction: (Long) -> String = Injekt.get()
        assertEquals("The long is 10", longStringFunction(10))
    }

    class Int1Action(val function: (Int) -> Int) {
        operator fun invoke(i: Int): Int = function(i)
    }

    @Test fun testInjectFunctionWrapper() {
        val action: Int1Action = Injekt.get()
        assertEquals(22, action(2))
    }

    class MyThing {
        val action1: (Int) -> Int by injectLazy()
        val action2: Int1Action by injectLazy()
    }

    @Test fun testInjectFunctionByDelegates() {
        val thing = MyThing()
        assertEquals(3, thing.action1(2))
        assertEquals(22, thing.action2(2))
    }
}