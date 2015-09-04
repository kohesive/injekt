package uy.kohesive.injekt.api

import java.lang.reflect.Type

@suppress("NOTHING_TO_INLINE")
public interface InjektRegistry {
    public final inline fun <reified T : Any> addSingleton(forClass: Class<T>, singleInstance: T) {
        addSingleton(fullType<T>(), singleInstance)
    }

    public final inline fun <reified R: Any> addSingletonFactory(forClass: Class<R>, @noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(fullType<R>(), factoryCalledOnce)
    }

    public final inline fun <reified R: Any> addFactory(forClass: Class<R>, @noinline factoryCalledEveryTime: () -> R) {
        addFactory(fullType<R>(), factoryCalledEveryTime)
    }

    public final inline fun <reified R: Any> addPerThreadFactory(forClass: Class<R>, @noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(fullType<R>(), factoryCalledOncePerThread)
    }

    public final inline fun <reified R: Any, reified K: Any> addPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, @noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
    }

    public final inline fun <reified R: Any, K: Any> addPerKeyFactory(forClass: Class<R>, @noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
    }

    public final inline fun <reified R: Any, reified K: Any> addPerThreadPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, @noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R: Any, reified K: Any> addPerThreadPerKeyFactory(forClass: Class<R>, @noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R : Any> addLoggerFactory(forLoggerClass: Class<R>, @noinline factoryByName: (String) -> R, @noinline factoryByClass: (Class<Any>) -> R)  {
        addLoggerFactory(fullType<R>(), factoryByName, factoryByClass)
    }

    public final inline fun <reified O: Any, reified T: O> addAlias(existingRegisteredClass: Class<T>, otherAncestorOrInterface: Class<O>) {
        addAlias(fullType<T>(), fullType<O>())
    }

    public final inline fun <reified T: Any> hasFactory(forClass: Class<T>): Boolean {
        return hasFactory(fullType<T>())
    }

    public final inline fun <reified T: Any> hasFactory(): Boolean {
        return hasFactory(fullType<T>())
    }

    public fun <T : Any> addSingleton(forType: TypeReference<T>, singleInstance: T)
    public fun <R: Any> addSingletonFactory(forType: TypeReference<R>, factoryCalledOnce: () -> R)
    public fun <R: Any> addFactory(forType: TypeReference<R>, factoryCalledEveryTime: () -> R)
    public fun <R: Any> addPerThreadFactory(forType: TypeReference<R>, factoryCalledOncePerThread: () -> R)
    public fun <R: Any, K: Any> addPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKey: (K) -> R)
    public fun <R: Any, K: Any> addPerThreadPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKeyPerThread: (K) -> R)
    public fun <R : Any> addLoggerFactory(forLoggerType: TypeReference<R>, factoryByName: (String) -> R, factoryByClass: (Class<Any>) -> R)
    public fun <O: Any, T: O> addAlias(existingRegisteredType: TypeReference<T>, otherAncestorOrInterface: TypeReference<O>)
    public fun <T: Any> hasFactory(forType: TypeReference<T>): Boolean

    public final inline fun <reified T : Any> T.registerAsSingleton() {
        addSingleton(fullType<T>(), this)
    }

    public final inline fun <reified T : Any> addSingleton(singleInstance: T) {
        addSingleton(fullType<T>(), singleInstance)
    }

    public final inline fun <reified R: Any> addSingletonFactory(@noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(fullType<R>(), factoryCalledOnce)
    }

    public final inline fun <reified R: Any> addFactory(@noinline factoryCalledEveryTime: () -> R) {
        addFactory(fullType<R>(), factoryCalledEveryTime)
    }

    public final inline fun <reified R: Any> addPerThreadFactory(@noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(fullType<R>(), factoryCalledOncePerThread)
    }

    public final inline fun <reified R: Any, K: Any> addPerKeyFactory(@noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
    }

    public final inline fun <reified R: Any, K: Any> addPerThreadPerKeyFactory(@noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R: Any> addLoggerFactory(@noinline factoryByName: (String) -> R, @noinline factoryByClass: (Class<Any>) -> R) {
        addLoggerFactory(fullType<R>(), factoryByName, factoryByClass)
    }
}
