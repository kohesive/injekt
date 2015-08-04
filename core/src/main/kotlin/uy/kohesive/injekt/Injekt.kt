package uy.kohesive.injekt

import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import kotlin.properties.Delegates
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KClass


public object Injekt: InjektRegistrar, InjektRegistry {
    public var registry: InjektInstanceFactory = DefaultInjektRegistry

    public object Property {
        public inline fun <reified T> lazy(): ReadOnlyProperty<Any?, T> {
            return LazyInjektVal {
                registry.getInstance(javaClass<T>())
            }
        }

        public inline fun <reified T> value(): ReadOnlyProperty<Any?, T> {
            val value: T = registry.getInstance(javaClass<T>())
            return object : ReadOnlyProperty<Any?, T> {
                public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
                    return value
                }
            }
        }

        public inline fun <reified T> lazy(key: Any): ReadOnlyProperty<Any?, T> {
            return LazyInjektVal {
                registry.getKeyedInstance(javaClass<T>(), key)
            }
        }

        public inline fun <reified T> value(key: Any): ReadOnlyProperty<Any?, T> {
            val value: T = registry.getKeyedInstance(javaClass<T>(), key)
            return object : ReadOnlyProperty<Any?, T> {
                public override fun get(thisRef: Any?, desc: PropertyMetadata): T {
                    return value
                }
            }
        }

        public inline fun <reified R, reified T> logger(): ReadOnlyProperty<R, T> {
            val value: T = registry.getLogger(javaClass<T>(), javaClass<R>())
            return object : ReadOnlyProperty<R, T> {
                public override fun get(thisRef: R, desc: PropertyMetadata): T {
                    return value
                }
            }
        }

        public inline fun <reified R, reified T> logger(byClass: Class<*>): ReadOnlyProperty<R, T> {
            val value: T = registry.getLogger(javaClass<T>(), byClass)
            return object : ReadOnlyProperty<R, T> {
                public override fun get(thisRef: R, desc: PropertyMetadata): T {
                    return value
                }
            }
        }

        public inline fun <reified R, reified T> logger(byName: String): ReadOnlyProperty<R, T> {
            val value: T = registry.getLogger(javaClass<T>(), byName)
            return object : ReadOnlyProperty<R, T> {
                public override fun get(thisRef: R, desc: PropertyMetadata): T {
                    return value
                }
            }
        }

    }

    public inline fun <reified T> get(): T = registry.getInstance(javaClass<T>())
    public inline fun <reified T> get(key: Any): T = registry.getKeyedInstance(javaClass<T>(), key)

    public fun <T> get(forClass: Class<T>): T = registry.getInstance(forClass)
    public fun <T> get(forClass: Class<T>, key: Any): T = registry.getKeyedInstance(forClass, key)

    public inline fun <reified T> logger(byClass: Class<*>): T = registry.getLogger(javaClass<T>(),byClass)
    public inline fun <reified T> logger(byName: String): T = registry.getLogger(javaClass<T>(),byName)
    public inline fun <reified T> logger(byObject: Any): T = registry.getLogger(javaClass<T>(),byObject.javaClass)
}

public inline fun <reified T> Delegates.injektLazy(): ReadOnlyProperty<Any?, T>  = Injekt.Property.lazy()
public inline fun <reified T> Delegates.injektValue(): ReadOnlyProperty<Any?, T> = Injekt.Property.value()
public inline fun <reified T> Delegates.injektLazy(key: Any): ReadOnlyProperty<Any?, T> = Injekt.Property.lazy(key)
public inline fun <reified T> Delegates.injektValue(key: Any): ReadOnlyProperty<Any?, T> = Injekt.Property.value(key)
public inline fun <reified R, reified T> Delegates.injektLogger(): ReadOnlyProperty<R, T> = Injekt.Property.logger()
public inline fun <reified R, reified T> Delegates.injektLogger(byClass: Class<*>): ReadOnlyProperty<R, T> = Injekt.Property.logger(byClass)
public inline fun <reified R, reified T> Delegates.injektLogger(byName: String): ReadOnlyProperty<R, T> = Injekt.Property.logger(byName)

public interface InjektInstanceFactory : InjektRegistry {
    public fun <R> getInstance(forClass: Class<R>): R
    public fun <R,K> getKeyedInstance(forClass: Class<R>, key: K): R
    public fun <R> getLogger(expectedLoggerClass: Class<R>, name: String): R
    public fun <R> getLogger(expectedLoggerClass: Class<R>, forClass: Class<*>): R
}
