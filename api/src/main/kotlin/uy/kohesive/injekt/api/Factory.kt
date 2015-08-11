package uy.kohesive.injekt.api

public interface InjektFactory {
    public fun <R> getInstance(forClass: Class<R>): R
    public fun <R, K> getKeyedInstance(forClass: Class<R>, key: K): R
    public fun <R> getLogger(expectedLoggerClass: Class<R>, name: String): R
    public fun <R> getLogger(expectedLoggerClass: Class<R>, forClass: Class<*>): R

    public final inline fun <reified T> get(): T = getInstance(javaClass<T>())
    public final inline fun <reified T> get(key: Any): T = getKeyedInstance(javaClass<T>(), key)

    public final fun <T> get(forClass: Class<T>): T = getInstance(forClass)
    public final fun <T> get(forClass: Class<T>, key: Any): T = getKeyedInstance(forClass, key)

    public final inline fun <reified T> logger(byClass: Class<*>): T = getLogger(javaClass<T>(), byClass)
    public final inline fun <reified T> logger(byName: String): T = getLogger(javaClass<T>(), byName)
    public final inline fun <reified T> logger(byObject: Any): T = getLogger(javaClass<T>(), byObject.javaClass)
}

