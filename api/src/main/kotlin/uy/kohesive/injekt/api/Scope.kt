package uy.kohesive.injekt.api

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty


/**
 * Not much difference than a InjektRegistrar for now...
 */
public open class InjektScope(val registrar: InjektRegistrar) : InjektRegistrar by registrar {}

public inline fun <reified T: Any> InjektScope.injectLazy(): Lazy<T> {
    return lazy { getInstance(fullType<T>()) }
}

public inline fun <reified T: Any> InjektScope.injectValue(): Lazy<T> {
    return lazyOf( getInstance(fullType<T>()))
}

public inline fun <reified T: Any> InjektScope.injectLazy(key: Any): Lazy<T> {
    return lazy { getKeyedInstance(fullType<T>(), key) }
}

public inline fun <reified T: Any> InjektScope.injectValue(key: Any): Lazy<T> {
    return lazyOf(getKeyedInstance(fullType<T>(), key))
}

public inline fun <reified T: Any, O: Any> InjektScope.injectLogger(forClass: Class<O>): Lazy<T> {
    return lazy { getLogger(fullType<T>(), forClass) }
}

public inline fun <reified R: Any, reified T: Any> InjektScope.injectLogger(byName: String): Lazy<T> {
    return lazy { getLogger(fullType<T>(), byName) }
}



@deprecated("use function on InjektScope instead: scope.injectLazy(): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectLazy(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    return Delegates.lazy { scope.getInstance(fullType<T>()) }
}

@deprecated("use function on InjektScope instead: scope.injectValue(): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectValue(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    val value: T = scope.getInstance(fullType<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use function on InjektScope instead: scope.injectLazy(key): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectLazy(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        scope.getKeyedInstance(fullType<T>(), key)
    }
}

@deprecated("use function on InjektScope instead: scope.injectValue(key): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectValue(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = scope.getKeyedInstance(fullType<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use function on InjektScope instead: scope.injectLogger(): Lazy<T>")
public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(scope: InjektScope): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(fullType<T>(), R::class.java)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use function on InjektScope instead: scope.injectLogger(forClass): Lazy<T>")
public inline fun <reified R: Any, reified T: Any, O: Any> Delegates.injectLogger(scope: InjektScope, forClass: Class<O>): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(fullType<T>(), forClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}

@deprecated("use function on InjektScope instead: scope.injectLogger(byName): Lazy<T>")
public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(scope: InjektScope, byName: String): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(fullType<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}