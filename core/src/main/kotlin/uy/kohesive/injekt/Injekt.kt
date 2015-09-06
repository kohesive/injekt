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

@deprecated("use toplevel function injectLazy(): Lazy<T> instead")
public inline fun <reified T: Any> Delegates.injectLazy(): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy { Injekt.getInstance(fullType<T>()) }
}

@deprecated("use toplevel function injectValue(): Lazy<T> instead")
public inline fun <reified T: Any> Delegates.injectValue(): ReadOnlyProperty<Any?, T> {
    val value: T = Injekt.getInstance(fullType<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use toplevel function injectLazy(key): Lazy<T> instead")
public inline fun <reified T: Any> Delegates.injectLazy(key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        Injekt.getKeyedInstance(fullType<T>(), key)
    }
}

@deprecated("use toplevel function injectValue(key): Lazy<T> instead")
public inline fun <reified T: Any> Delegates.injectValue(key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = Injekt.getKeyedInstance(fullType<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use toplevel function injectLogger(): Lazy<T> instead")
public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(fullType<T>(), R::class.java)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use toplevel function injectLogger(forClass): Lazy<T> instead")
public inline fun <reified R: Any, reified T: Any, O: Any> Delegates.injectLogger(forClass: Class<O>): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(fullType<T>(), forClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use toplevel function injectLogger(byName): Lazy<T> instead")
public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(byName: String): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(fullType<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}


public inline fun <reified T: Any> injectLazy(): Lazy<T> {
    return lazy { Injekt.getInstance(fullType<T>()) }
}

public inline fun <reified T: Any> injectValue(): Lazy<T> {
    return lazyOf(Injekt.getInstance(fullType<T>()))
}

public inline fun <reified T: Any> injectLazy(key: Any): Lazy<T> {
    return lazy { Injekt.getKeyedInstance(fullType<T>(), key) }
}

public inline fun <reified T: Any> injectValue(key: Any): Lazy<T> {
    return lazyOf(Injekt.getKeyedInstance(fullType<T>(), key))
}

public inline fun <reified R: Any, reified T: Any> R.injectLogger(): Lazy<T> {
    return lazy { Injekt.getLogger(fullType<T>(), R::class.java) }
}

public inline fun <reified T: Any, O: Any> injectLogger(forClass: Class<O>): Lazy<T> {
    return lazy { Injekt.getLogger(fullType<T>(), forClass) }
}

public inline fun <reified R: Any, reified T: Any> injectLogger(byName: String): Lazy<T> {
    return lazy { Injekt.getLogger(fullType<T>(), byName) }
}

