package uy.kohesive.injekt

import org.junit.Test
import kotlin.test.assertEquals

class TestGuthub35 {

    @Test fun testInjectFunctionType() {
        Injekt.addSingletonFactory {
            val function: (Int) -> Int = { value -> value + 1 }
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
        Injekt.addSingletonFactory {
            val function: (Int) -> Int = { value -> value + 20 }
            Int1Action(function)
        }

        val action: Int1Action = Injekt.get()
        assertEquals(22, action(2))
    }
}