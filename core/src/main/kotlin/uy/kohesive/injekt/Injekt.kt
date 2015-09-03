package uy.kohesive.injekt

import uy.kohesive.injekt.api.InjektScope
import uy.kohesive.injekt.api.InjektScopedMain
import uy.kohesive.injekt.api.fullType
import uy.kohesive.injekt.registry.default.DefaultRegistrar
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty

public volatile var Injekt: InjektScope = InjektScope(DefaultRegistrar())

/**
 * A class that startups up an system using Injekt, using the default global scope
 */
public abstract class InjektMain : InjektScopedMain(Injekt)

// top level Injekt scope

public inline fun <reified T: Any> Delegates.injectLazy(): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy { Injekt.getInstance(fullType<T>()) }
}

public inline fun <reified T: Any> Delegates.injectValue(): ReadOnlyProperty<Any?, T> {
    val value: T = Injekt.getInstance(fullType<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified T: Any> Delegates.injectLazy(key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        Injekt.getKeyedInstance(fullType<T>(), key)
    }
}

public inline fun <reified T: Any> Delegates.injectValue(key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = Injekt.getKeyedInstance(fullType<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(fullType<T>(), javaClass<R>())
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R: Any, reified T: Any, O: Any> Delegates.injectLogger(forClass: Class<O>): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(fullType<T>(), forClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(byName: String): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(fullType<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

