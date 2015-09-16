package uy.kohesive.injekt.api

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
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



@Deprecated("use function on InjektScope instead: scope.injectLazy(): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectLazy(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    return Delegates.lazy { scope.get(fullType<T>()) }
}

@Deprecated("use function on InjektScope instead: scope.injectValue(): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectValue(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    val value: T = scope.get(fullType<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, property: PropertyMetadata): T {
            return value
        }
    }
}

@Deprecated("use function on InjektScope instead: scope.injectLazy(key): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectLazy(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        scope.get(fullType<T>(), key)
    }
}

@Deprecated("use function on InjektScope instead: scope.injectValue(key): Lazy<T>")
public inline fun <reified T: Any> Delegates.injectValue(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = scope.get(fullType<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, property: PropertyMetadata): T {
            return value
        }
    }
}

@Deprecated("use function on InjektScope instead: scope.injectLogger(): Lazy<T>")
public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(scope: InjektScope): ReadOnlyProperty<R, T> {
    val value: T = scope.logger(fullType<T>(), R::class.java)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}

@Deprecated("use function on InjektScope instead: scope.injectLogger(forClass): Lazy<T>")
public inline fun <reified R: Any, reified T: Any, O: Any> Delegates.injectLogger(scope: InjektScope, forClass: Class<O>): ReadOnlyProperty<R, T> {
    val value: T = scope.logger(fullType<T>(), forClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}

@Deprecated("use function on InjektScope instead: scope.injectLogger(byName): Lazy<T>")
public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(scope: InjektScope, byName: String): ReadOnlyProperty<R, T> {
    val value: T = scope.logger(fullType<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, property: PropertyMetadata): T {
            return value
        }
    }
}