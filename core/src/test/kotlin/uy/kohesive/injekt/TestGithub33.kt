package uy.kohesive.injekt


import org.junit.Test
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.addSingleton
import uy.kohesive.injekt.api.addSingletonFactory
import uy.kohesive.injekt.api.get
import kotlin.test.assertEquals
import kotlin.test.fail

class TestGithub33 {
    data class Store<T1, T2>(val state: T1, val obj: T2)
    data class ApplicationState(val id: String)
    data class ObjType1(val id: String)
    data class ObjType2(val id: String)
    data class OtherState(val id: String)

    companion object : InjektMain() {
        override fun InjektRegistrar.registerInjectables() {
            addSingleton(Store(ApplicationState("as1"), ObjType1("ot1")))
            addSingleton(Store(ApplicationState("as2"), ObjType2("ot2")))
            addSingleton(Store(OtherState("os1"), ObjType1("ot1")))
            addSingletonFactory<Store<ApplicationState, Any>> { Store(ApplicationState("as3"), ObjType2("ot2")) }
        }
    }

    @Test
    fun testWithGenerics() {
        val check1: Store<ApplicationState, ObjType1> = Injekt.get()
        assertEquals(Store(ApplicationState("as1"), ObjType1("ot1")), check1)

        val check2: Store<ApplicationState, ObjType2> = Injekt.get()
        assertEquals(Store(ApplicationState("as2"), ObjType2("ot2")), check2)

        val check3: Store<OtherState, ObjType1> = Injekt.get()
        assertEquals(Store(OtherState("os1"), ObjType1("ot1")), check3)

        val check4: Store<ApplicationState, Any> = Injekt.get()
        assertEquals(Store<ApplicationState, Any>(ApplicationState("as3"), ObjType2("ot2")), check4)
    }

    @Test
    fun testDoesNotExistWithGenericType() {
        try {
            val check: Store<OtherState, Any> = Injekt.get()
            fail("Should not exist, this type")
        } catch (ex: Throwable) {
            // pass
        }
    }
}
