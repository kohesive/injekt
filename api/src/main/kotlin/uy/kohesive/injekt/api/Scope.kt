package uy.kohesive.injekt.api

import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty


/**
 * Not much difference than a InjektRegistrar for now...
 */
public open class InjektScope(val registrar: InjektRegistrar) : InjektRegistrar by registrar{
    override fun <T> alias(existingRegisteredClass: Class<T>, otherClassesThatAreSame: List<Class<*>>) {
        if (!hasFactory(existingRegisteredClass)) {
            throw InjektionException("Cannot alias anything to  ${existingRegisteredClass.getName()}, it does not have a registered factory")
        }
        for (oneOther in otherClassesThatAreSame) {
            if (!oneOther.isAssignableFrom(existingRegisteredClass)) {
                throw InjektionException("Cannot alias ${oneOther.getName()} to ${existingRegisteredClass.getName()}, not compatible types")
            }
        }
        registrar.alias(existingRegisteredClass, otherClassesThatAreSame)
    }

}

public inline fun <reified T> Delegates.injectLazy(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy { scope.getInstance(javaClass<T>()) }
}

public inline fun <reified T> Delegates.injectValue(scope: InjektScope): ReadOnlyProperty<Any?, T> {
    val value: T = scope.getInstance(javaClass<T>())
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified T> Delegates.injectLazy(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    return kotlin.properties.Delegates.lazy {
        scope.getKeyedInstance(javaClass<T>(), key)
    }
}

public inline fun <reified T> Delegates.injectValue(scope: InjektScope, key: Any): ReadOnlyProperty<Any?, T> {
    val value: T = scope.getKeyedInstance(javaClass<T>(), key)
    return object : ReadOnlyProperty<Any?, T> {
        public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R, reified T> Delegates.injectLogger(scope: InjektScope): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(javaClass<T>(), javaClass<R>())
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R, reified T> Delegates.injectLogger(scope: InjektScope, byClass: Class<*>): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(javaClass<T>(), byClass)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}

public inline fun <reified R, reified T> Delegates.injectLogger(scope: InjektScope, byName: String): ReadOnlyProperty<R, T> {
    val value: T = scope.getLogger(javaClass<T>(), byName)
    return object : ReadOnlyProperty<R, T> {
        public override fun get(thisRef: R, desc: PropertyMetadata): T {
            return value
        }
    }
}