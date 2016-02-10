@file:Suppress("NOTHING_TO_INLINE")

package uy.kohesive.injekt.api

import kotlin.reflect.KClass


/**
 * Not much difference than a InjektRegistrar for now...
 */
public open class InjektScope(val registrar: InjektRegistrar) : InjektRegistrar by registrar {}

public inline fun <reified T: Any> InjektScope.injectLazy(): Lazy<T> {
    return lazy { get(fullType<T>()) }
}

public inline fun <reified T: Any> InjektScope.injectValue(): Lazy<T> {
    return lazyOf( get(fullType<T>()))
}

public inline fun <reified T: Any> InjektScope.injectLazy(key: Any): Lazy<T> {
    return lazy { get(fullType<T>(), key) }
}

public inline fun <reified T: Any> InjektScope.injectValue(key: Any): Lazy<T> {
    return lazyOf(get(fullType<T>(), key))
}

public inline fun <reified T: Any, O: Any> InjektScope.injectLogger(forClass: Class<O>): Lazy<T> {
    return lazy { logger(fullType<T>(), forClass) }
}

public inline fun <reified T: Any, O: Any> InjektScope.injectLogger(forClass: KClass<O>): Lazy<T> {
    return lazy { logger(fullType<T>(), forClass.java) }
}

public inline fun <reified R: Any, reified T: Any> InjektScope.injectLogger(byName: String): Lazy<T> {
    return lazy { logger(fullType<T>(), byName) }
}

inline fun <reified R: Any> InjektScope.addScopedSingletonFactory(noinline scopedFactoryCalledOnce: InjektScope.() -> R) {
    addSingletonFactory(fullType<R>()) { this.scopedFactoryCalledOnce()  }
}

inline fun <reified R: Any> InjektScope.addScopedFactory(noinline scopedFactoryCalledEveryTime: InjektScope.() -> R) {
    addFactory(fullType<R>()) { this.scopedFactoryCalledEveryTime()  }
}

inline fun <reified R: Any, K: Any> InjektScope.addScopedPerKeyFactory(noinline scopedFactoryCalledPerKey: InjektScope.(key: K) -> R) {
    addPerKeyFactory(fullType<R>()) { key: K -> this.scopedFactoryCalledPerKey(key)  }
}

inline fun <reified R: Any, K: Any> InjektScope.addScopedPerThreadPerKeyFactory(noinline scopedFactoryCalledPerKeyPerThread: InjektScope.(key: K) -> R) {
    addPerThreadPerKeyFactory(fullType<R>()) { key: K -> this.scopedFactoryCalledPerKeyPerThread(key)  }
}

inline fun <reified R: Any> InjektScope.addScopedPerThreadFactory(noinline scopedFactoryCalledPerThread: InjektScope.() -> R) {
    addPerThreadFactory(fullType<R>()) { this.scopedFactoryCalledPerThread()  }
}


