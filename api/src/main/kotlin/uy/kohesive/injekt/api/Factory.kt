package uy.kohesive.injekt.api

import java.lang.reflect.Type
import kotlin.reflect.KClass

@Suppress("NOTHING_TO_INLINE")
public interface InjektFactory {
    public final inline fun <R: Any> getInstance(forType: TypeReference<R>): R {
        return getInstance(forType.type)
    }
    public final inline fun <R: Any, K: Any> getKeyedInstance(forType: TypeReference<R>, key: K): R  {
        return getKeyedInstance(forType.type, key)
    }
    public final inline fun <R: Any> getLogger(expectedLoggerType: TypeReference<R>, byName: String): R {
        return getLogger(expectedLoggerType.type, byName)
    }
    public final inline fun <R: Any, T: Any> getLogger(expectedLoggerType: TypeReference<R>, forClass: Class<T>): R {
        return getLogger(expectedLoggerType.type, forClass)
    }

    public final inline fun <R: Any, T: Any> getLogger(expectedLoggerType: TypeReference<R>, forClass: KClass<T>): R {
        return getLogger(expectedLoggerType.type, forClass.java)
    }

    public final inline fun <reified R: Any> get(forType: TypeReference<R>): R = getInstance(forType.type)

    public final inline fun <reified R: Any> get(forType: TypeReference<R>, key: Any): R = getKeyedInstance(forType.type, key)

    public fun <R: Any> getInstance(forType: Type): R
    public fun <R: Any, K: Any> getKeyedInstance(forType: Type, key: K): R
    public fun <R: Any> getLogger(expectedLoggerType: Type, byName: String): R
    public fun <R: Any, T: Any> getLogger(expectedLoggerType: Type, forClass: Class<T>): R

    public final inline fun <reified R: Any> get(): R = getInstance(fullType<R>().type)
    public final inline fun <reified R: Any> get(key: Any): R = getKeyedInstance(fullType<R>().type, key)
    public final inline fun <reified R: Any, T: Any> logger(forClass: Class<T>): R = getLogger(fullType<R>().type, forClass)
    public final inline fun <reified R: Any, T: Any> logger(forClass: KClass<T>): R = getLogger(fullType<R>().type, forClass.javaClass)
    public final inline fun <reified R: Any> logger(byName: String): R = getLogger(fullType<R>().type, byName)
    public final inline fun <reified R: Any> logger(byObject: Any): R = getLogger(fullType<R>().type, byObject.javaClass)


    @Deprecated("Use getInstance(fullType<MyClass<WithGenerics>>()) or type infered ge<T>t() otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> getInstance(forClass: Class<R>): R = getInstance(fullType<R>())

    @Deprecated("Use getKeyedInstance(fullType<MyClass<WithGenerics>>()) or type infered get<T>() otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any, reified K: Any> getKeyedInstance(forClass: Class<R>, key: K): R = getKeyedInstance(fullType<R>(), key)

    @Deprecated("Use getLogger(fullType<LoggerClass>(), byName) or type infered logger<T>(byName) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> getLogger(expectedLoggerClass: Class<R>, byName: String): R = getLogger(fullType<R>(), byName)

    @Deprecated("Use getLogger(fullType<LoggerClass>(), forClass) or type infered logger<T>(forClass) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any, reified T: Any> getLogger(expectedLoggerClass: Class<R>, forClass: Class<T>): R = getLogger(fullType<R>(), forClass)

    @Deprecated("Use getInstance(fullType<MyClass<WithGenerics>>()) or type infered get<T>() otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> get(forClass: Class<R>): R = getInstance(fullType<R>())

    @Deprecated("Use getInstance(fullType<MyClass<WithGenerics>>(), key) or type infered get<T>(key) otherwise possibly suffer generic type erasure")
    public final inline fun <reified R: Any> get(forClass: Class<R>, key: Any): R = getKeyedInstance(fullType<R>(), key)


}

