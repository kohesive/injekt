@file:Suppress("NOTHING_TO_INLINE")

package uy.kohesive.injekt.api

public interface InjektRegistry {
    public fun <T : Any> addSingleton(forType: TypeReference<T>, singleInstance: T)
    public fun <R: Any> addSingletonFactory(forType: TypeReference<R>, factoryCalledOnce: () -> R)
    public fun <R: Any> addFactory(forType: TypeReference<R>, factoryCalledEveryTime: () -> R)
    public fun <R: Any> addPerThreadFactory(forType: TypeReference<R>, factoryCalledOncePerThread: () -> R)
    public fun <R: Any, K: Any> addPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKey: (K) -> R)
    public fun <R: Any, K: Any> addPerThreadPerKeyFactory(forType: TypeReference<R>, factoryCalledPerKeyPerThread: (K) -> R)
    public fun <R : Any> addLoggerFactory(forLoggerType: TypeReference<R>, factoryByName: (String) -> R, factoryByClass: (Class<Any>) -> R)
    public fun <O: Any, T: O> addAlias(existingRegisteredType: TypeReference<T>, otherAncestorOrInterface: TypeReference<O>)
    public fun <T: Any> hasFactory(forType: TypeReference<T>): Boolean
}

public inline fun <reified T: Any> InjektRegistry.hasFactory(): Boolean {
    return hasFactory(fullType<T>())
}

public inline fun <reified T : Any> InjektRegistry.addSingleton(singleInstance: T) {
    addSingleton(fullType<T>(), singleInstance)
}

public inline fun <reified R: Any> InjektRegistry.addSingletonFactory(noinline factoryCalledOnce: () -> R) {
    addSingletonFactory(fullType<R>(), factoryCalledOnce)
}

public inline fun <reified R: Any> InjektRegistry.addFactory(noinline factoryCalledEveryTime: () -> R) {
    addFactory(fullType<R>(), factoryCalledEveryTime)
}

public inline fun <reified R: Any> InjektRegistry.addPerThreadFactory(noinline factoryCalledOncePerThread: () -> R) {
    addPerThreadFactory(fullType<R>(), factoryCalledOncePerThread)
}

public inline fun <reified R: Any, K: Any> InjektRegistry.addPerKeyFactory(noinline factoryCalledPerKey: (K) -> R) {
    addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
}

public inline fun <reified R: Any, K: Any> InjektRegistry.addPerThreadPerKeyFactory(noinline factoryCalledPerKeyPerThread: (K) -> R) {
    addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
}

public inline fun <reified R: Any> InjektRegistry.addLoggerFactory(noinline factoryByName: (String) -> R, noinline factoryByClass: (Class<Any>) -> R) {
    addLoggerFactory(fullType<R>(), factoryByName, factoryByClass)
}

public inline fun <reified EXISTINGREGISTERED: ANCESTORTYPE, reified ANCESTORTYPE: Any> InjektRegistry.addAlias() = addAlias(fullType<EXISTINGREGISTERED>(), fullType<ANCESTORTYPE>())