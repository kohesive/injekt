@file:Suppress("NOTHING_TO_INLINE")

package uy.kohesive.injekt.api

import kotlin.reflect.KClass


/**
 * Not much difference than a InjektRegistrar for now...
 */
@Suppress("DEPRECATION")
open class InjektScope(val registrar: InjektRegistrar) : InjektRegistrar by registrar {
    fun importModule(submodule: InjektModule) {
        submodule.registerWith(this)
    }

    inline fun <reified T : Any> injectLazy(): Lazy<T> {
        return lazy { get(fullType<T>()) }
    }

    inline fun <reified T : Any> injectValue(): Lazy<T> {
        return lazyOf(get(fullType<T>()))
    }

    inline fun <reified T : Any> injectLazy(key: Any): Lazy<T> {
        return lazy { get(fullType<T>(), key) }
    }

    inline fun <reified T : Any> injectValue(key: Any): Lazy<T> {
        return lazyOf(get(fullType<T>(), key))
    }

    inline fun <reified T : Any, O : Any> injectLogger(forClass: Class<O>): Lazy<T> {
        return lazy { logger(fullType<T>(), forClass) }
    }

    inline fun <reified T : Any, O : Any> injectLogger(forClass: KClass<O>): Lazy<T> {
        return lazy { logger(fullType<T>(), forClass.java) }
    }

    inline fun <reified R : Any, reified T : Any> injectLogger(byName: String): Lazy<T> {
        return lazy { logger(fullType<T>(), byName) }
    }

    inline fun <reified R : Any> addScopedSingletonFactory(noinline scopedFactoryCalledOnce: InjektScope.() -> R) {
        addSingletonFactory(fullType<R>()) { this.scopedFactoryCalledOnce() }
    }

    inline fun <reified R : Any> addScopedFactory(noinline scopedFactoryCalledEveryTime: InjektScope.() -> R) {
        addFactory(fullType<R>()) { this.scopedFactoryCalledEveryTime() }
    }

    inline fun <reified R : Any, K : Any> addScopedPerKeyFactory(noinline scopedFactoryCalledPerKey: InjektScope.(key: K) -> R) {
        addPerKeyFactory(fullType<R>()) { key: K -> this.scopedFactoryCalledPerKey(key) }
    }

    inline fun <reified R : Any, K : Any> addScopedPerThreadPerKeyFactory(noinline scopedFactoryCalledPerKeyPerThread: InjektScope.(key: K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>()) { key: K -> this.scopedFactoryCalledPerKeyPerThread(key) }
    }

    inline fun <reified R : Any> addScopedPerThreadFactory(noinline scopedFactoryCalledPerThread: InjektScope.() -> R) {
        addPerThreadFactory(fullType<R>()) { this.scopedFactoryCalledPerThread() }
    }

    // ===========================================================
    // registry extension methods repeated here for findability
    // ===========================================================

    inline fun <reified T: Any> hasFactory(): Boolean {
        return hasFactory(fullType<T>())
    }

    inline fun <reified T : Any> addSingleton(singleInstance: T) {
        addSingleton(fullType<T>(), singleInstance)
    }

    inline fun <reified R: Any> addSingletonFactory(noinline factoryCalledOnce: () -> R) {
        addSingletonFactory(fullType<R>(), factoryCalledOnce)
    }

    inline fun <reified R: Any> addFactory(noinline factoryCalledEveryTime: () -> R) {
        addFactory(fullType<R>(), factoryCalledEveryTime)
    }

    inline fun <reified R: Any> addPerThreadFactory(noinline factoryCalledOncePerThread: () -> R) {
        addPerThreadFactory(fullType<R>(), factoryCalledOncePerThread)
    }

    inline fun <reified R: Any, K: Any> addPerKeyFactory(noinline factoryCalledPerKey: (K) -> R) {
        addPerKeyFactory(fullType<R>(), factoryCalledPerKey)
    }

    inline fun <reified R: Any, K: Any> addPerThreadPerKeyFactory(noinline factoryCalledPerKeyPerThread: (K) -> R) {
        addPerThreadPerKeyFactory(fullType<R>(), factoryCalledPerKeyPerThread)
    }

    inline fun <reified R: Any> addLoggerFactory(noinline factoryByName: (String) -> R, noinline factoryByClass: (Class<Any>) -> R) {
        addLoggerFactory(fullType<R>(), factoryByName, factoryByClass)
    }

    inline fun <reified EXISTINGREGISTERED: ANCESTORTYPE, reified ANCESTORTYPE: Any> InjektRegistry.addAlias() = addAlias(fullType<EXISTINGREGISTERED>(), fullType<ANCESTORTYPE>())

    // ===========================================================
    // factory extension methods repeated here for findability
    // ===========================================================

    inline fun <R: Any> get(forType: TypeReference<R>): R = getInstance(forType.type)
    inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, default: R): R = getInstanceOrElse(forType.type, default)
    inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, noinline default: ()->R): R = getInstanceOrElse(forType.type, default)
    inline fun <reified R: Any> getOrNull(forType: TypeReference<R>): R? = getInstanceOrNull(forType.type)

    inline operator fun <reified R: Any> invoke(): R = getInstance(fullType<R>().type)
    inline fun <reified R: Any> get(): R = getInstance(fullType<R>().type)
    inline fun <reified R: Any> getOrElse(default: R): R = getInstanceOrElse(fullType<R>().type, default)
    inline fun <reified R: Any> getOrElse(noinline default: ()->R): R = getInstanceOrElse(fullType<R>().type, default)
    inline fun <reified R: Any> getOrNull(): R? = getInstanceOrNull(fullType<R>().type)

    inline fun <R: Any> get(forType: TypeReference<R>, key: Any): R = getKeyedInstance(forType.type, key)
    inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, key: Any, default: R): R = getKeyedInstanceOrElse(forType.type, key, default)
    inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, key: Any, noinline default: ()->R): R = getKeyedInstanceOrElse(forType.type, key, default)
    inline fun <reified R: Any> getOrNull(forType: TypeReference<R>, key: Any): R? = getKeyedInstanceOrNull(forType.type, key)

    inline fun <reified R: Any> get(key: Any): R = getKeyedInstance(fullType<R>().type, key)
    inline fun <reified R: Any> getOrElse(key: Any, default: R): R = getKeyedInstanceOrElse(fullType<R>().type, key, default)
    inline fun <reified R: Any> getOrElse(key: Any, noinline default: ()->R): R = getKeyedInstanceOrElse(fullType<R>().type, key, default)
    inline fun <reified R: Any> getOrNull(key: Any): R? = getKeyedInstanceOrNull(fullType<R>().type, key)

    inline fun <R: Any, T: Any> logger(expectedLoggerType: TypeReference<R>, forClass: Class<T>): R = getLogger(expectedLoggerType.type, forClass)
    inline fun <reified R: Any, T: Any> logger(forClass: Class<T>): R = getLogger(fullType<R>().type, forClass)

    inline fun <R: Any, T: Any> logger(expectedLoggerType: TypeReference<R>, forClass: KClass<T>): R = getLogger(expectedLoggerType.type, forClass.java)
    inline fun <reified R: Any, T: Any> logger(forClass: KClass<T>): R = getLogger(fullType<R>().type, forClass.java)

    inline fun <R: Any> logger(expectedLoggerType: TypeReference<R>, byName: String): R = getLogger(expectedLoggerType.type, byName)
    inline fun <reified R: Any> logger(byName: String): R = getLogger(fullType<R>().type, byName)

    inline fun <R: Any> logger(expectedLoggerType: TypeReference<R>, byObject: Any): R = getLogger(expectedLoggerType.type, byObject.javaClass)
    inline fun <reified R: Any> logger(byObject: Any): R = getLogger(fullType<R>().type, byObject.javaClass)
}

abstract class LocalScoped(protected val localScope: InjektScope) {
    inline fun <reified T: Any> injectLazy(): Lazy<T> {
        return localScope.injectLazy()
    }

    inline fun <reified T: Any> injectValue(): Lazy<T> {
        return localScope.injectValue()
    }

    inline fun <reified T: Any> injectLazy(key: Any): Lazy<T> {
        return localScope.injectLazy(key)
    }

    inline fun <reified T: Any> injectValue(key: Any): Lazy<T> {
        return localScope.injectValue(key)
    }

    // injection of logger is intentionally not done, it could be from local scope, but more likely global so should be explicit or in descendant class.
}


