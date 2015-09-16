package uy.kohesive.injekt.api

import java.lang.reflect.Type
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE")
public interface InjektFactory {
    @Deprecated("Use get(forType)")
    public final inline fun <R: Any> getInstance(forType: TypeReference<R>): R = getInstance(forType.type)

    @Deprecated("Use get(forType, key)")
    public final inline fun <R: Any, K: Any> getKeyedInstance(forType: TypeReference<R>, key: K): R = getKeyedInstance(forType.type, key)

    @Deprecated("Use logger(forLoggerType, byName)")
    public final inline fun <R: Any> getLogger(expectedLoggerType: TypeReference<R>, byName: String): R = getLogger(expectedLoggerType.type, byName)

    @Deprecated("Use logger(forLoggerType, forClass)")
    public final inline fun <R: Any, T: Any> getLogger(expectedLoggerType: TypeReference<R>, forClass: Class<T>): R = getLogger(expectedLoggerType.type, forClass)

    @Deprecated("Use logger(forLoggerType, forClass)")
    public final inline fun <R: Any, T: Any> getLogger(expectedLoggerType: TypeReference<R>, forClass: KClass<T>): R = getLogger(expectedLoggerType.type, forClass.java)


    public final inline fun <R: Any> get(forType: TypeReference<R>): R = getInstance(forType.type)
    public final inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, default: R): R = getInstanceOrElse(forType.type, default)
    public final inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, noinline default: ()->R): R = getInstanceOrElse(forType.type, default)

    public final inline fun <reified R: Any> get(): R = getInstance(fullType<R>().type)
    public final inline fun <reified R: Any> getOrElse(default: R): R = getInstanceOrElse(fullType<R>().type, default)
    public final inline fun <reified R: Any> getOrElse(noinline default: ()->R): R = getInstanceOrElse(fullType<R>().type, default)

    public final inline fun <R: Any> get(forType: TypeReference<R>, key: Any): R = getKeyedInstance(forType.type, key)
    public final inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, key: Any, default: R): R = getKeyedInstanceOrElse(forType.type, key, default)
    public final inline fun <reified R: Any> getOrElse(forType: TypeReference<R>, key: Any, noinline default: ()->R): R = getKeyedInstanceOrElse(forType.type, key, default)

    public final inline fun <reified R: Any> get(key: Any): R = getKeyedInstance(fullType<R>().type, key)
    public final inline fun <reified R: Any> getOrElse(key: Any, default: R): R = getKeyedInstanceOrElse(fullType<R>().type, key, default)
    public final inline fun <reified R: Any> getOrElse(key: Any, noinline default: ()->R): R = getKeyedInstanceOrElse(fullType<R>().type, key, default)

    public fun <R: Any> getInstance(forType: Type): R
    public fun <R: Any> getInstanceOrElse(forType: Type, default: R): R
    public fun <R: Any> getInstanceOrElse(forType: Type, default: ()->R): R

    public fun <R: Any, K: Any> getKeyedInstance(forType: Type, key: K): R
    public fun <R: Any, K: Any> getKeyedInstanceOrElse(forType: Type, key: K, default: R): R
    public fun <R: Any, K: Any> getKeyedInstanceOrElse(forType: Type, key: K, default: ()->R): R

    public fun <R: Any> getLogger(expectedLoggerType: Type, byName: String): R
    public fun <R: Any, T: Any> getLogger(expectedLoggerType: Type, forClass: Class<T>): R

    public final inline fun <R: Any, T: Any> logger(expectedLoggerType: TypeReference<R>, forClass: Class<T>): R = getLogger(expectedLoggerType.type, forClass)
    public final inline fun <reified R: Any, T: Any> logger(forClass: Class<T>): R = getLogger(fullType<R>().type, forClass)

    public final inline fun <R: Any, T: Any> logger(expectedLoggerType: TypeReference<R>, forClass: KClass<T>): R = getLogger(expectedLoggerType.type, forClass.java)
    public final inline fun <reified R: Any, T: Any> logger(forClass: KClass<T>): R = getLogger(fullType<R>().type, forClass.java)

    public final inline fun <R: Any> logger(expectedLoggerType: TypeReference<R>, byName: String): R = getLogger(expectedLoggerType.type, byName)
    public final inline fun <reified R: Any> logger(byName: String): R = getLogger(fullType<R>().type, byName)

    public final inline fun <R: Any> logger(expectedLoggerType: TypeReference<R>, byObject: Any): R = getLogger(expectedLoggerType.type, byObject.javaClass)
    public final inline fun <reified R: Any> logger(byObject: Any): R = getLogger(fullType<R>().type, byObject.javaClass)


    @Deprecated("Use getInstance(fullType<MyClass<WithGenerics>>()) or type infered ge<T>t() otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> getInstance(forClass: Class<R>): R = get(fullType<R>())

    @Deprecated("Use getKeyedInstance(fullType<MyClass<WithGenerics>>()) or type infered get<T>() otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any, reified K: Any> getKeyedInstance(forClass: Class<R>, key: K): R = get(fullType<R>(), key)

    @Deprecated("Use getLogger(fullType<LoggerClass>(), byName) or type infered logger<T>(byName) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> getLogger(expectedLoggerClass: Class<R>, byName: String): R = logger(fullType<R>(), byName)

    @Deprecated("Use getLogger(fullType<LoggerClass>(), forClass) or type infered logger<T>(forClass) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any, reified T: Any> getLogger(expectedLoggerClass: Class<R>, forClass: Class<T>): R = logger(fullType<R>(), forClass)

    @Deprecated("Use getInstance(fullType<MyClass<WithGenerics>>()) or type infered get<T>() otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> get(forClass: Class<R>): R = get(fullType<R>())

    @Deprecated("Use getInstance(fullType<MyClass<WithGenerics>>(), key) or type infered get<T>(key) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> get(forClass: Class<R>, key: Any): R = get(fullType<R>(), key)


}

