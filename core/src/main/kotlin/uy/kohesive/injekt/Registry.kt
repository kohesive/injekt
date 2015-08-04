package uy.kohesive.injekt

import kotlin.reflect.KClass


public interface InjektRegistry {
    public fun <T: Any> addSingleton(forClass: Class<T>, singleInstance: T)
    public fun <R> addSingletonFactory(forClass: Class<R>, factoryCalledOnce: ()->R)
    public fun <R> addFactory(forClass: Class<R>, factoryCalledEveryTime: ()->R)
    public fun <R> addPerThreadFactory(forClass: Class<R>, factoryCalledOncePerThread: ()->R)
    public fun <R, K> addPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKey: (K)->R)
    public fun <R, K> addPerThreadPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKeyPerThread: (K)->R)
    public fun <R: Any> addLoggerFactory(forLoggerClass: Class<R>, factoryByName: (String)->R, factoryByClass: (Class<*>)->R)
    public fun <T> alias(existingRegisteredClass: Class<T>, otherClassesThatAreSame: List<Class<*>>)
    public fun <T> hasFactory(forClass: Class<T>): Boolean
}

public interface InjektRegistrar : InjektRegistry {
    fun importInjektables(submodule: Injektables) {
        submodule.registerWith(this)
    }

    public final inline fun <reified T: Any> T.registerAsSingleton(singleInstance: T) { addSingleton(singleInstance) }
    public final inline fun <reified R> KClass<R>.registerSingletonFactory(@noinline factoryCalledOnce: ()->R) { addSingletonFactory(factoryCalledOnce) }
    public final inline fun <reified R> KClass<R>.registerFactory(@noinline factoryCalledEveryTime: ()->R) { addFactory(factoryCalledEveryTime) }
    public final inline fun <reified R> KClass<R>.registerPerThreadFactory(@noinline factoryCalledOncePerThread: ()->R) { addPerThreadFactory(factoryCalledOncePerThread) }
    public final inline fun <reified R, reified K> KClass<R>.registerPerKeyFactory(@noinline factoryCalledPerKey: (K)->R) { addPerKeyFactory(factoryCalledPerKey) }
    public final inline fun <reified R, reified K> KClass<R>.registerPerThreadPerKeyFactory(@noinline factoryCalledPerKeyPerThread: (K)->R) { addPerThreadPerKeyFactory(factoryCalledPerKeyPerThread) }
    public final inline fun <reified R> KClass<R>.registerLoggerFactory(@noinline factoryByName: (String)->R, @noinline factoryByClass: (Class<*>)->R) { addLoggerFactory(factoryByName, factoryByClass) }

    public final inline fun <reified T: Any> T.aliasOthersToMe(classes: List<Class<*>>) {
        alias(javaClass<T>(), classes)
    }

    public final inline fun <reified T: Any> addSingleton(singleInstance: T) {
        Injekt.registry.addSingleton(javaClass<T>(), singleInstance)
    }

    public final inline fun <reified R> addSingletonFactory(@noinline factoryCalledOnce: ()->R) {
        Injekt.registry.addSingletonFactory(javaClass<R>(), factoryCalledOnce)
    }

    public final inline fun <reified R> addFactory(@noinline factoryCalledEveryTime: ()->R) {
        Injekt.registry.addFactory(javaClass<R>(), factoryCalledEveryTime)
    }

    public final inline fun <reified R> addPerThreadFactory(@noinline factoryCalledOncePerThread: ()->R) {
        Injekt.registry.addPerThreadFactory(javaClass<R>(), factoryCalledOncePerThread)
    }

    public final inline fun <reified R, reified K> addPerKeyFactory(@noinline factoryCalledPerKey: (K)->R) {
        Injekt.registry.addPerKeyFactory(javaClass<R>(), javaClass<K>(), factoryCalledPerKey)
    }

    public final inline fun <reified R, reified K> addPerThreadPerKeyFactory(@noinline factoryCalledPerKeyPerThread: (K)->R) {
        Injekt.registry.addPerThreadPerKeyFactory(javaClass<R>(), javaClass<K>(), factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R> addLoggerFactory(@noinline factoryByName: (String)->R, @noinline factoryByClass: (Class<*>)->R) {
        Injekt.registry.addLoggerFactory(javaClass<R>(), factoryByName, factoryByClass)
    }

    override fun <T: Any> addSingleton(forClass: Class<T>, singleInstance: T) {
        Injekt.registry.addSingleton(forClass, singleInstance)
    }

    override fun <R> addSingletonFactory(forClass: Class<R>, factoryCalledOnce: () -> R) {
        Injekt.registry.addSingletonFactory(forClass, factoryCalledOnce)
    }

    override fun <R> addFactory(forClass: Class<R>, factoryCalledEveryTime: () -> R) {
        Injekt.registry.addFactory(forClass, factoryCalledEveryTime)
    }

    override fun <R> addPerThreadFactory(forClass: Class<R>, factoryCalledOncePerThread: () -> R) {
        Injekt.registry.addPerThreadFactory(forClass, factoryCalledOncePerThread)
    }

    override fun <R, K> addPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKey: (K) -> R) {
        Injekt.registry.addPerKeyFactory(forClass, forKeyClass, factoryCalledPerKey)
    }

    override fun <R, K> addPerThreadPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKeyPerThread: (K) -> R) {
        Injekt.registry.addPerThreadPerKeyFactory(forClass, forKeyClass, factoryCalledPerKeyPerThread)
    }

    override fun <R: Any> addLoggerFactory(forLoggerClass: Class<R>, factoryByName: (String) -> R, factoryByClass: (Class<*>) -> R) {
        Injekt.registry.addLoggerFactory(forLoggerClass, factoryByName, factoryByClass)
    }

    override fun <T> alias(existingRegisteredClass: Class<T>, otherClassesThatAreSame: List<Class<*>>) {
        if (!hasFactory(existingRegisteredClass)) {
            throw InjektionException("Cannot alias anything to  ${existingRegisteredClass.getName()}, it does not have a registered factory")
        }
        for (oneOther in otherClassesThatAreSame) {
            if (!oneOther.isAssignableFrom(existingRegisteredClass)) {
                throw InjektionException("Cannot alias ${oneOther.getName()} to ${existingRegisteredClass.getName()}, not compatible types")
            }
        }
        Injekt.registry.alias(existingRegisteredClass, otherClassesThatAreSame)
    }

    override fun <T> hasFactory(forClass: Class<T>): Boolean {
        return Injekt.registry.hasFactory(forClass)
    }
}


class InjektionException(msg: String): RuntimeException(msg)
