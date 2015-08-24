package uy.kohesive.injekt.config.typesafe

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigResolveOptions
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import uy.kohesive.injekt.api.InjektScope
import java.net.URI
import kotlin.properties.Delegates

/**
 * A class that startups up an system using Injekt + TypesafeConfig, using the default global scope, and default object binder
 */
public abstract class KonfigAndInjektMain(): KonfigAndInjektScopedMain(Injekt)

/**
 *  A startup module that registers and uses singletons/object factories from a specific scope,
 *  and an ObjectMapper to bind configuration properties into class instances.
 */
public abstract class KonfigAndInjektScopedMain(public val scope: InjektScope, public val mapper: ObjectMapper = jacksonObjectMapper()) : InjektModule, KonfigModule {
    private val ADDON_ID = "Konfigure"

    protected var resolvedConfig: Config by Delegates.notNull()

    abstract fun configFactory(): Config

    private data class KonfigureClassAtPath(val path: String, val klass: Class<*>)

    private inner class ScopedKonfigRegistrar(val path: List<String>, val scope: InjektScope, val itemsToConfigure: MutableList<KonfigureClassAtPath>): KonfigRegistrar, InjektRegistrar by scope {
//        override fun lazyImportModule(atPath: String, module: KonfigModule) {
//            module.registerWith(ScopedKonfigRegistrar(path + atPath.split('.'), scope, itemsToConfigure))
//        }

        override fun importModule(atPath: String, module: KonfigModule) {
            val tempItemsToConfigure: MutableList<KonfigureClassAtPath> = linkedListOf()
            module.registerWith(ScopedKonfigRegistrar(path + atPath.split('.'), scope, tempItemsToConfigure))
            loadAndInject(resolvedConfig, tempItemsToConfigure)
        }

//        override fun lazyBindClassAtConfigRoot(klass: Class<*>) {
//            val fullpath = path.filter { it.isNotBlank() }.map { it.removePrefix(".").removeSuffix(".") }.joinToString(".")
//            itemsToConfigure.add(KonfigureClassAtPath(fullpath, klass))
//        }

//        override fun lazyBindClassAtConfigPath(configPath: String, klass: Class<*>) {
//            val fullpath = (path + configPath.split('.')).filter { it.isNotBlank() }.map { it.removePrefix(".").removeSuffix(".") }.joinToString(".")
//            itemsToConfigure.add(KonfigureClassAtPath(fullpath, klass))
//        }

        override fun bindClassAtConfigRoot(klass: Class<*>) {
            val fullpath = path.filter { it.isNotBlank() }.map { it.removePrefix(".").removeSuffix(".") }.joinToString(".")
            loadAndInject(resolvedConfig, listOf(KonfigureClassAtPath(fullpath, klass)))
        }

        override fun bindClassAtConfigPath(configPath: String, klass: Class<*>) {
            val fullpath = (path + configPath.split('.')).filter { it.isNotBlank() }.map { it.removePrefix(".").removeSuffix(".") }.joinToString(".")
            loadAndInject(resolvedConfig, listOf(KonfigureClassAtPath(fullpath, klass)))
        }

        @suppress("UNCHECKED_CAST")
        fun loadAndInject(config: Config, whichItemsToConfigure: List<KonfigureClassAtPath>) {
            whichItemsToConfigure.forEach {
                val configAtPath = config.getConfig(it.path)
                val asJson = configAtPath.root().render(ConfigRenderOptions.concise().setJson(true))
                val instance: Any = mapper.readValue(asJson, it.klass)!!
                scope.registrar.addSingleton(it.klass as Class<Any>, instance)
            }
        }
    }

    init {
        val itemsToConfigure: MutableList<KonfigureClassAtPath> = scope.getAddonMetadata(ADDON_ID) ?: scope.setAddonMetadata(ADDON_ID, linkedListOf<KonfigureClassAtPath>())
        resolvedConfig = configFactory()
        val registrar = ScopedKonfigRegistrar(emptyList(), scope, itemsToConfigure)
        registrar.registerConfigurables()
        registrar.loadAndInject(resolvedConfig, itemsToConfigure)
        itemsToConfigure.clear()
        scope.registrar.registerInjectables()
    }
}

public interface KonfigRegistrar: InjektRegistrar {
    /**
     * import a module, config binding is deferred until an outer module causes a load, or end of the
     * configuration registration
     */
 //   fun lazyImportModule(atPath: String, module: KonfigModule)

    /**
     * import a module loading it and any submodules immediately
     */
    fun importModule(atPath: String, module: KonfigModule)

    /**
     * bind a class at a configuration path, deferring binding until a module causes a load, or end of
     * configuration chain
     */
//    final inline fun <reified T> lazyBindClassAtConfigPath(configPath: String) {
//        lazyBindClassAtConfigPath(configPath, javaClass<T>())
//    }

    /**
     * bind a class bindings its values from a configuration path immediately
     */
    final inline fun <reified T> bindClassAtConfigPath(configPath: String) {
        bindClassAtConfigPath(configPath, javaClass<T>())
    }

    /**
     * bind a class at a configuration path, deferring binding until a module causes a load, or end of
     * configuration chain
     */
//    fun lazyBindClassAtConfigPath(configPath: String, klass: Class<*>)

    /**
     * bind a class bindings its values from a configuration path immediately
     */
    fun bindClassAtConfigPath(configPath: String, klass: Class<*>)

    /**
     * bind a class at root of current configuration path, deferring binding until a module causes a load,
     * or end of configuration chain
     */
//    final inline fun <reified T> lazyBindClassAtConfigRoot() {
//        lazyBindClassAtConfigRoot(javaClass<T>())
//    }

    /**
     * bind a class bindings its values from the root of the current configuration path immediately
     */
    final inline fun <reified T> bindClassAtConfigRoot() {
        bindClassAtConfigRoot(javaClass<T>())
    }

    /**
     * bind a class at root of current configuration path, deferring binding until a module causes a load,
     * or end of configuration chain
     */
//    fun lazyBindClassAtConfigRoot(klass: Class<*>)

    /**
     * bind a class bindings its values from the root of the current configuration path immediately
     */
    fun bindClassAtConfigRoot(klass: Class<*>)
}

/**
 * A package of configuration bound items that can be included into a scope of someone else
 */
public interface KonfigModule {
    final internal fun registerWith(intoModule: KonfigRegistrar) {
        intoModule.registerConfigurables()
    }

    fun KonfigRegistrar.registerConfigurables()
}

