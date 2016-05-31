package uy.kohesive.injekt

import org.junit.Test
import kotlin.test.assertEquals

class TestGuthub35 {

    @Test fun testInjectFunctionType() {
        Injekt.addSingletonFactory {
            val function: (Int) -> Int = { value -> value + 1 }
            function
        }

        val function: (Int) -> Int = Injekt.get()
        assertEquals(3, function(2))
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