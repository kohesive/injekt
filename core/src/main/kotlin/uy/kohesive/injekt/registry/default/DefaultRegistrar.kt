package uy.kohesive.injekt.registry.default

import uy.kohesive.injekt.InjektRegistrar
import uy.kohesive.injekt.InjektionException
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap

/**
 * Default implementation of registry that uses ConcurrentHashMaps which have zero or few locks during reads, and work well
 * in a write little, read many model.  Which is exactly our model.  This stores the factories and the resulting instances
 * for cases that keep them around (Singletons, Per Key instances, Per Thread instances)
 */
public open class DefaultRegistrar : InjektRegistrar {
    private enum class FactoryType { SINGLETON, MULTI, MULTIKEYED, THREAD, THREADKEYED }

    private val NOKEY = object {}

    data class Instance(val forWhatClass: Class<*>, val forKey: Any)
    data class ThreadKey(val forThread: Thread, val forKey: Any)

    private val existingValues = ConcurrentHashMap<Instance, Any>()
    private val factories = ConcurrentHashMap<Class<*>, () -> Any>()
    private val keyedFactories = ConcurrentHashMap<Class<*>, (Any) -> Any>()

    data class LoggerInfo(val forWhatClass: Class<*>, val nameFactory: (String) -> Any, val classFactory: (Class<*>) -> Any)

    private volatile var loggerFactory: LoggerInfo? = null

    override fun <T> hasFactory(forClass: Class<T>): Boolean {
        return factories.get(forClass) != null || keyedFactories.get(forClass) != null
    }

    override fun <T> alias(existingRegisteredClass: Class<T>, otherClassesThatAreSame: List<Class<*>>) {
        // factories existing or not, and data type compatibility is checked in the InjektRegistrar interface default methods
        val existingFactory = factories.get(existingRegisteredClass)
        val existingKeyedFactory = keyedFactories.get(existingRegisteredClass)

        if (existingFactory != null) {
            for (oneOther in otherClassesThatAreSame) {
                factories.put(oneOther, existingFactory)
            }
        }
        if (existingKeyedFactory != null) {
            for (oneOther in otherClassesThatAreSame) {
                keyedFactories.put(oneOther, existingKeyedFactory)
            }
        }
    }

    override fun <T : Any> addSingleton(forClass: Class<T>, singleInstance: T) {
        addSingletonFactory(forClass, { singleInstance })
        getInstance(forClass)
    }

    override fun <R> addSingletonFactory(forClass: Class<R>, factoryCalledOnce: () -> R) {
        factories.put(forClass, { existingValues.concurrentGetOrPutProxy(Instance(forClass, NOKEY), { factoryCalledOnce() }) })
    }

    override fun <R> addFactory(forClass: Class<R>, factoryCalledEveryTime: () -> R) {
        factories.put(forClass, factoryCalledEveryTime)
    }

    override fun <R> addPerThreadFactory(forClass: Class<R>, factoryCalledOncePerThread: () -> R) {
        factories.put(forClass, {
            existingValues.concurrentGetOrPutProxy(Instance(forClass, ThreadKey(Thread.currentThread(), NOKEY)), { factoryCalledOncePerThread() })
        })
    }

    @suppress("UNCHECKED_CAST")
    override fun <R, K> addPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKey: (K) -> R) {
        keyedFactories.put(forClass, {
            key ->
            existingValues.concurrentGetOrPutProxy(Instance(forClass, key), { factoryCalledPerKey(key as K) })
        })
    }

    @suppress("UNCHECKED_CAST")
    override fun <R, K> addPerThreadPerKeyFactory(forClass: Class<R>, forKeyClass: Class<K>, factoryCalledPerKeyPerThread: (K) -> R) {
        keyedFactories.put(forClass, {
            key ->
            existingValues.concurrentGetOrPutProxy(Instance(forClass, ThreadKey(Thread.currentThread(), key)), { factoryCalledPerKeyPerThread(key as K) })
        })
    }

    override fun <R : Any> addLoggerFactory(forLoggerClass: Class<R>, factoryByName: (String) -> R, factoryByClass: (Class<*>) -> R) {
        loggerFactory = LoggerInfo(forLoggerClass, factoryByName, factoryByClass)
    }

    @suppress("UNCHECKED_CAST")
    override fun <R> getInstance(forClass: Class<R>): R {
        val factory = factories.get(forClass) ?: throw InjektionException("No registered instance or factory for class ${forClass.getName()}")
        return factory.invoke() as R
    }

    @suppress("UNCHECKED_CAST")
    override fun <R, K> getKeyedInstance(forClass: Class<R>, key: K): R {
        val factory = keyedFactories.get(forClass) ?: throw InjektionException("No registered keyed factory for class ${forClass.getName()}")
        return factory.invoke(key) as R
    }

    private fun assertLogger(expectedLoggerClass: Class<*>) {
        if (loggerFactory == null) {
            throw InjektionException("Cannot call getLogger() -- A logger factory has not been registered with Injekt")
        } else if (!expectedLoggerClass.isAssignableFrom(loggerFactory!!.forWhatClass)) {
            throw InjektionException("Logger factories registered with Injekt indicate they return ${loggerFactory!!.forWhatClass.getName()} but current injekt target is expecting ${expectedLoggerClass.getName()}")
        }
    }

    @suppress("UNCHECKED_CAST")
    override fun <R> getLogger(expectedLoggerClass: Class<R>, name: String): R {
        assertLogger(expectedLoggerClass)
        return loggerFactory!!.nameFactory(name) as R   // if casting to wrong type, let it die with casting exception
    }

    @suppress("UNCHECKED_CAST")
    override fun <R> getLogger(expectedLoggerClass: Class<R>, forClass: Class<*>): R {
        assertLogger(expectedLoggerClass)
        return loggerFactory!!.classFactory(forClass) as R  // if casting to wrong type, let it die with casting exception
    }


}

