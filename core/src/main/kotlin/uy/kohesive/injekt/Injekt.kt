package uy.kohesive.injekt

import uy.kohesive.injekt.registry.default.DefaultRegistrar
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty

public volatile var Injekt: InjektScope = InjektScope(DefaultRegistrar())

public abstract class InjektMain : InjektScopedMain(Injekt)

// top level Injekt scope

public inline fun <reified T> Delegates.injectLazy(): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy { Injekt.getInstance(javaClass<T>()) }
}

public inline fun <reified T> Delegates.injectValue(): ReadOnlyProperty<Any?, T> {
    val value: T = Injekt.getInstance(javaClass<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified T> Delegates.injectLazy(key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        Injekt.getKeyedInstance(javaClass<T>(), key)
    }
}

public inline fun <reified T> Delegates.injectValue(key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = Injekt.getKeyedInstance(javaClass<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R, reified T> Delegates.injectLogger(): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(javaClass<T>(), javaClass<R>())
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R, reified T> Delegates.injectLogger(byClass: Class<*>): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(javaClass<T>(), byClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R, reified T> Delegates.injectLogger(byName: String): ReadOnlyProperty<R, T> {
    val value: T = Injekt.getLogger(javaClass<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

// to be removed

@deprecated("use Delegates.injectLazy") public inline fun <reified T> Delegates.injektLazy(): ReadOnlyProperty<Any?, T> = injectLazy()
@deprecated("use Delegates.injectValue") public inline fun <reified T> Delegates.injektValue(): ReadOnlyProperty<Any?, T> = injectValue()
@deprecated("use Delegates.injectLazy") public inline fun <reified T> Delegates.injektLazy(key: Any): ReadOnlyProperty<Any?, T> = injectLazy(key)
@deprecated("use Delegates.injectValue") public inline fun <reified T> Delegates.injektValue(key: Any): ReadOnlyProperty<Any?, T> = injectValue(key)
@deprecated("use Delegates.injectLogger") public inline fun <reified R, reified T> Delegates.injektLogger(): ReadOnlyProperty<R, T> = injectLogger()
@deprecated("use Delegates.injectLogger") public inline fun <reified R, reified T> Delegates.injektLogger(byClass: Class<*>): ReadOnlyProperty<R, T> = injectLogger(byClass)
@deprecated("use Delegates.injectLogger") public inline fun <reified R, reified T> Delegates.injektLogger(byName: String): ReadOnlyProperty<R, T> = injectLogger(byName)
