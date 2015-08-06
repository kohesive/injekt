package uy.kohesive.injekt

import kotlin.properties
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty

public inline fun <reified T> Delegates.injektLazy(): ReadOnlyProperty<Any?, T>  = Injekt.Property.lazy()
public inline fun <reified T> Delegates.injektValue(): ReadOnlyProperty<Any?, T> = Injekt.Property.value()
public inline fun <reified T> Delegates.injektLazy(key: Any): ReadOnlyProperty<Any?, T> = Injekt.Property.lazy(key)
public inline fun <reified T> Delegates.injektValue(key: Any): ReadOnlyProperty<Any?, T> = Injekt.Property.value(key)
public inline fun <reified R, reified T> Delegates.injektLogger(): ReadOnlyProperty<R, T> = Injekt.Property.logger()
public inline fun <reified R, reified T> Delegates.injektLogger(byClass: Class<*>): ReadOnlyProperty<R, T> = Injekt.Property.logger(byClass)
public inline fun <reified R, reified T> Delegates.injektLogger(byName: String): ReadOnlyProperty<R, T> = Injekt.Property.logger(byName)

// based on code from Kotlin Delegates class

private object NULL_VALUE {}

private fun escape(value: Any?): Any {
    return value ?: NULL_VALUE
}

@suppress("UNCHECKED_CAST")
private fun <T> unescape(value: Any?): T {
    return if (value == NULL_VALUE) null as T else value as T
}

public class LazyInjektVal<T>(private val initializer: (ref: Any?) -> T) : ReadOnlyProperty<Any?, T> {
    private var value: Any? = null

    public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
        if (value == null) {
            value = escape(initializer(thisRef))
        }
        return unescape<T>(value)
    }
}
