package uy.kohesive.injekt

import kotlin.properties
import kotlin.properties.ReadOnlyProperty

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
