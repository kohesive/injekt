package uy.kohesive.injekt.api

import kotlin.reflect.KClass

public interface InjektRegistry {
    public fun <T : Any> addSingleton(forClass: Class<T>, singleInstance: T)
    public fun <R> addSingletonFactory(forClass: Class<R>, factoryCalledOnce: () -> R)
    public fun <R> addFactory(forClass: Class<R>, factoryCalledEveryTime: () -> R)
    public fun <R> addPerThreadFactory(forClass: Class<R>, factoryCalledOncePerThread: () -> R)
    public fun <R, K> addPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKey: (K) -> R)
    public fun <R, K> addPerThreadPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKeyPerThread: (K) -> R)
    public fun <R : Any> addLoggerFactory(forLoggerClass: Class<R>, factoryByName: (String) -> R, factoryByClass: (Class<*>) -> R)
    public fun <T> alias(existingRegisteredClass: Class<T>, otherClassesThatAreSame: List<Class<*>>)
    public fun <T> hasFactory(forClass: Class<T>): Boolean

    public fun <T> getAddonMetadata(addon: String): T
    public fun <T> setAddonMetadata(addon: String, metadata: T): T

    public final inline fun <reified T : Any> T.registerAsSingleton() {
        addSingleton(this)
    }

    public final inline fun <reified R> KClass<R>.registerSingletonFactory(@noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(factoryCalledOnce)
    }

    public final inline fun <reified R> KClass<R>.registerFactory(@noinline factoryCalledEveryTime: () -> R) {
        addFactory(factoryCalledEveryTime)
    }

    public final inline fun <reified R> KClass<R>.registerPerThreadFactory(@noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(factoryCalledOncePerThread)
    }

    public final inline fun <reified R, reified K> KClass<R>.registerPerKeyFactory(@noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(factoryCalledPerKey)
    }

    public final inline fun <reified R, reified K> KClass<R>.registerPerThreadPerKeyFactory(@noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R> KClass<R>.registerLoggerFactory(@noinline factoryByName: (String) -> R, @noinline factoryByClass: (Class<*>) -> R) {
        addLoggerFactory(factoryByName, factoryByClass)
    }

    public final inline fun <reified T : Any> T.aliasOthersToMe(classes: List<Class<*>>) {
        alias(javaClass<T>(), classes)
    }

    public final inline fun <reified T : Any> addSingleton(singleInstance: T) {
        addSingleton(javaClass<T>(), singleInstance)
    }

    public final inline fun <reified R> addSingletonFactory(@noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(javaClass<R>(), factoryCalledOnce)
    }

    public final inline fun <reified R> addFactory(@noinline factoryCalledEveryTime: () -> R) {
        addFactory(javaClass<R>(), factoryCalledEveryTime)
    }

    public final inline fun <reified R> addPerThreadFactory(@noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(javaClass<R>(), factoryCalledOncePerThread)
    }

    public final inline fun <reified R, reified K> addPerKeyFactory(@noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(javaClass<R>(), javaClass<K>(), factoryCalledPerKey)
    }

    public final inline fun <reified R, reified K> addPerThreadPerKeyFactory(@noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(javaClass<R>(), javaClass<K>(), factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R> addLoggerFactory(@noinline factoryByName: (String) -> R, @noinline factoryByClass: (Class<*>) -> R) {
        addLoggerFactory(javaClass<R>(), factoryByName, factoryByClass)
    }
}
