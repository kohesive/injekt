package uy.kohesive.injekt.api

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty


/**
 * Not much difference than a InjektRegistrar for now...
 */
public open class InjektScope(val registrar: InjektRegistrar) : InjektRegistrar by registrar { }

public inline fun <reified T: Any> Delegates.injectLazy(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    return Delegates.lazy { scope.getInstance(fullType<T>()) }
}

public inline fun <reified T: Any> Delegates.injectValue(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    val value: T = scope.getInstance(fullType<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified T: Any> Delegates.injectLazy(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        scope.getKeyedInstance(fullType<T>(), key)
    }
}

public inline fun <reified T: Any> Delegates.injectValue(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = scope.getKeyedInstance(fullType<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(scope: InjektScope): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(fullType<T>(), javaClass<R>())
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R: Any, reified T: Any, O: Any> Delegates.injectLogger(scope: InjektScope, forClass: Class<O>): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(fullType<T>(), forClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R: Any, reified T: Any> Delegates.injectLogger(scope: InjektScope, byName: String): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(fullType<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}