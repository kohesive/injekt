package uy.kohesive.injekt.tests

import org.junit.Test
import uy.kohesive.injekt.*
import java.util.concurrent.ConcurrentLinkedQueue
import java.util.concurrent.CountDownLatch
import kotlin.platform.platformStatic
import kotlin.properties.Delegates
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class MockLogger(name: String?, clazz: Class<*>?) {
    val result: String = name ?: clazz?.getName() ?: "!!error!!"
}

class TestInjektion {
    companion object : InjektModule() {
        // platformStatic public fun main(args: Array<String>) {
            // the Injekt module goes on something that you know will be instantiated first, say the companion object of where you put
            // your static main.  Or the first class it creates.
        // }

        override fun InjektRegistrar.registerInjektables() {
            // import other prepackaged injecktions
            importInjektables(OtherModuleWithPrepackagedInjektions)
            importInjektables(ExtraModuleWithInjektions)

            // factory for one instance per thread
            addPerThreadFactory { NotThreadSafeConnection(Thread.currentThread().toString()) }
            // instantiate now, not later
            addSingleton(NotLazy("Freddy"))

            // register the descendant class to be created, allow any ancestor classes to be used
            addSingleton(DescendantThing("family"))
            alias(javaClass<DescendantThing>(), listOf(javaClass<AncestorThing>()))

            // logging is special!
            addLoggerFactory(javaClass<MockLogger>(), { name -> MockLogger(name,null) }, { klass -> MockLogger(null, klass) })
        }
    }

    // now we can inject using delegation in any class
    val swm: SomethingSingleton by Delegates.injektValue()  // inject at instantiation
    val many: ManyMultiples by Delegates.injektLazy() // inject when accessed
    val many2: ManyMultiples by Delegates.injektLazy()
    val worker: NotLazy by Delegates.injektLazy()

    val LOG: MockLogger by Delegates.injektLogger()
    val LOG_BYNAME: MockLogger by Delegates.injektLogger("testy")
    val LOG_BYCLASS: MockLogger by Delegates.injektLogger(javaClass<TestInjektion>())

    @Test public fun testInjectedMembers() {
        assertEquals("Hi, I'm single", swm.name)
        assertEquals(swm, Injekt.get<SomethingSingleton>()) // ask for a value directly
        assertEquals("Freddy", worker.name)
        assertEquals(worker, Injekt.get<NotLazy>()) // should always get same singletons
        assertTrue(worker.identityEquals( Injekt.get<NotLazy>()) ) // should always get same singletons

        assertNotEquals(many.whenCreated, many2.whenCreated)
        assertNotEquals(Injekt.get<ManyMultiples>(), Injekt.get<ManyMultiples>())
    }

    @Test public fun testInjectionOfThreadSingletons() {
        val sync = CountDownLatch(3)
        val threadVals = ConcurrentLinkedQueue<NotThreadSafeConnection>()
        for (i in 0..2) {
            Thread() {
                val myThreadValue1 = Injekt.get<NotThreadSafeConnection>()
                val myThreadValue2 = Injekt.get<NotThreadSafeConnection>()
                assertTrue(myThreadValue1.identityEquals(myThreadValue2))
                threadVals.add(myThreadValue1)
                sync.countDown()
            }.start()
        }
        sync.await()

        assertEquals(3, threadVals.size())
        val results = threadVals.toArrayList()
        assertNotEquals(results[0], results[1])
        assertNotEquals(results[0], results[2])
        assertNotEquals(results[1], results[2])
    }

    @Test public fun testInjectionInMethodParameters() {
        @data class ConstructedWithInjektion(val mySingleItem: SomethingSingleton = Injekt.get())

        fun doSomething(myWorker: NotLazy = Injekt.get()) = myWorker.name

        assertEquals("Hi, I'm single", ConstructedWithInjektion().mySingleItem.name)
        assertEquals("Freddy", doSomething())
    }

    @Test public fun testNestedInjection() {
        @data class ConstructedInFactory(val mySingleItem: SomethingSingleton)

        Companion.addSingletonFactory {  ConstructedInFactory(Injekt.get<SomethingSingleton>()) }

        assertEquals("Hi, I'm single", Injekt.get<ConstructedInFactory>().mySingleItem.name)
    }

    @Test public fun testAnyDescendantLevel() {
        assertEquals("family", Injekt.get<DescendantThing>().name)
        assertEquals("family", Injekt.get<AncestorThing>().name)
    }

    @Test public fun testLogging() {
        assertNotNull(LOG)
        assertEquals("uy.kohesive.injekt.tests.TestInjektion", LOG.result)
        assertNotNull(LOG_BYNAME)
        assertEquals("testy", LOG_BYNAME.result)
        assertNotNull(LOG_BYCLASS)
        assertEquals("uy.kohesive.injekt.tests.TestInjektion", LOG_BYCLASS.result)

        // sending in anything not a string or a class will use the Class of the item
        assertEquals("uy.kohesive.injekt.tests.NotLazy", Injekt.logger<MockLogger>(NotLazy("asd")).result)
    }

    @Test public fun testKeyedInjection() {
        Companion.addPerKeyFactory(javaClass<KeyedThing>(), javaClass<String>(), { key -> KeyedThing("$key - ${System.currentTimeMillis()}") })
        val one = Injekt.get<KeyedThing>("one")
        val two = Injekt.get<KeyedThing>("two")
        assertNotEquals(one,two)
        val oneAgain = Injekt.get<KeyedThing>("one")
        assertEquals(one, oneAgain)

    }


}

data class NotThreadSafeConnection(val whatThreadMadeMe: String)
data class NotLazy(val name: String)
data class KeyedThing(val name: String)

open data class AncestorThing(val name: String)
data class DescendantThing(name: String): AncestorThing(name)

// === code can make common things ready for injection in the best way possible, if these were other modules or packages
//     they have defined some importable injections:

object OtherModuleWithPrepackagedInjektions: Injektables {
    override fun InjektRegistrar.import() {
        // lazy factory for singleton
        addSingletonFactory { SomethingSingleton("Hi, I'm single") }
    }
}

data class SomethingSingleton(val name: String)

// === and more...

object ExtraModuleWithInjektions : Injektables {
    override fun InjektRegistrar.import() {
        // factory for new instance per use
        addFactory { ManyMultiples() }
    }
}

data class ManyMultiples(val whenCreated: Long = System.currentTimeMillis()) {
    init {
        Thread.sleep(1) // let's not create two with the same milliseconds for this test
    }
}

