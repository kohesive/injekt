package uy.kohesive.injekt.api

import java.lang.reflect.Type
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE")
public interface InjektRegistry {
    @Deprecated("Use addSingleton(fullType<MyClass<WithGenerics>>(), instance) or type infered addSingleton<T>(instance) otherwise possibly suffer generic type erasure")
    public final inline fun <reified T : Any> addSingleton(forClass: Class<T>, singleInstance: T) {
        addSingleton(fullType<T>(), singleInstance)
    }

    @Deprecated("Use addSingletonFactory(fullType<MyClass<WithGenerics>>(), factory) or type infered addSingletonFactory<T>(factory) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> addSingletonFactory(forClass: Class<R>, noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(fullType<R>(), factoryCalledOnce)
    }

    @Deprecated("Use addFactory(fullType<MyClass<WithGenerics>>(), factory) or type infered addFactory<T>(factory) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> addFactory(forClass: Class<R>, noinline factoryCalledEveryTime: () -> R) {
        addFactory(fullType<R>(), factoryCalledEveryTime)
    }

    @Deprecated("Use addPerThreadFactory(fullType<MyClass<WithGenerics>>(), factory) or type infered addPerThreadFactory<T>(factory) otherwise spossibly uffer generic type erasure")
    public final inline fun <reified R: Any> addPerThreadFactory(forClass: Class<R>, noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(fullType<R>(), factoryCalledOncePerThread)
    }

    @Deprecated("Use addPerThreadFactory(fullType<MyClass<WithGenerics>>(), factory) or type infered addPerThreadFactory<T>(factory) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any, K: Any> addPerKeyFactory(forClass: Class<R>, noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
    }

    @Deprecated("Use addPerThreadPerKeyFactory(fullType<MyClass<WithGenerics>>(), factory) or type infered addPerThreadPerKeyFactory<T>(factory) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any, reified K: Any> addPerThreadPerKeyFactory(forClass: Class<R>, noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
    }

    @Deprecated("Use addLoggerFactory(fullType<LoggerClass>(), factoryForClass, factoryByName) or type infered addLoggerFactory<T>(factoryForClass, factoryByName) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R : Any> addLoggerFactory(forLoggerClass: Class<R>, noinline factoryByName: (String) -> R, noinline factoryByClass: (Class<Any>) -> R)  {
        addLoggerFactory(fullType<R>(), factoryByName, factoryByClass)
    }


    @Deprecated("Use addAlias(fullType<MyClass<WithGenerics>>(), fullType<MyAncestorClass<WithGenerics>>()) otherwise possibly suffer generic type erasure")
    public final inline fun <reified O: Any, reified T: O> addAlias(existingRegisteredClass: Class<T>, otherAncestorOrInterface: Class<O>) {
        addAlias(fullType<T>(), fullType<O>())
    }

    @Deprecated("Use hasFactory<MyClass<WithGenerics>>() or hasFactory(fullType<MyClass<WithGenerics>>()) otherwise possibly suffer generic type erasure")
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

    public final inline fun <reified R: Any> addSingletonFactory(noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(fullType<R>(), factoryCalledOnce)
    }

    public final inline fun <reified R: Any> addFactory(noinline factoryCalledEveryTime: () -> R) {
        addFactory(fullType<R>(), factoryCalledEveryTime)
    }

    public final inline fun <reified R: Any> addPerThreadFactory(noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(fullType<R>(), factoryCalledOncePerThread)
    }

    public final inline fun <reified R: Any, K: Any> addPerKeyFactory(noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
    }

    public final inline fun <reified R: Any, K: Any> addPerThreadPerKeyFactory(noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
    }

    public final inline fun <reified R: Any> addLoggerFactory(noinline factoryByName: (String) -> R, noinline factoryByClass: (Class<Any>) -> R) {
        addLoggerFactory(fullType<R>(), factoryByName, factoryByClass)
    }

    public final inline fun <reified EXISTINGREGISTERED: ANCESTORTYPE, reified ANCESTORTYPE: Any> addAlias() = addAlias(fullType<EXISTINGREGISTERED>(), fullType<ANCESTORTYPE>())
}
