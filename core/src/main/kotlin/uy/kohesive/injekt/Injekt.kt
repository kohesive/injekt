@file:Suppress("NOTHING_TO_INLINE")

package uy.kohesive.injekt

import uy.kohesive.injekt.api.*
import uy.kohesive.injekt.registry.default.DefaultRegistrar
import kotlin.reflect.KClass

public @Volatile var Injekt: InjektScope = InjektScope(DefaultRegistrar())

/**
 * A class that startups up a system using Injekt, using the default global scope
 */
public abstract class InjektMain : InjektScopedMain(Injekt)

public inline fun <reified T : Any> injectLazy(): Lazy<T> {
    return lazy { Injekt.get(fullType<T>()) }
}

public inline fun <reified T : Any> injectValue(): Lazy<T> {
    return lazyOf(Injekt.get(fullType<T>()))
}

public inline fun <reified T : Any> injectLazy(key: Any): Lazy<T> {
    return lazy { Injekt.get(fullType<T>(), key) }
}

public inline fun <reified T : Any> injectValue(key: Any): Lazy<T> {
    return lazyOf(Injekt.get(fullType<T>(), key))
}

public inline fun <reified R : Any, reified T : Any> R.injectLogger(): Lazy<T> {
    return lazy { Injekt.logger(fullType<T>(), R::class.java) }
}

public inline fun <reified T : Any, O : Any> injectLogger(forClass: KClass<O>): Lazy<T> {
    return lazy { Injekt.logger(fullType<T>(), forClass.java) }
}

public inline fun <reified T : Any, O : Any> injectLogger(forClass: Class<O>): Lazy<T> {
    return lazy { Injekt.logger(fullType<T>(), forClass) }
}

public inline fun <reified T : Any> injectLogger(byName: String): Lazy<T> {
    return lazy { Injekt.logger(fullType<T>(), byName) }
}

