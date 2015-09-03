package uy.kohesive.injekt.config.typesafe

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.type.TypeFactory
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import com.typesafe.config.ConfigRenderOptions
import com.typesafe.config.ConfigResolveOptions
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.*
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

    private inner class ScopedKonfigRegistrar(val path: List<String>, val scope: InjektScope): KonfigRegistrar, InjektRegistrar by scope {
        override fun importModule(atPath: String, module: KonfigModule) {
            module.registerWith(ScopedKonfigRegistrar(path + atPath.split('.'), scope))
        }

        override fun <T: Any> bindClassAtConfigRoot(klass: TypeReference<T>) {
            val fullpath = path.filter { it.isNotBlank() }.map { it.removePrefix(".").removeSuffix(".") }.joinToString(".")
            loadAndInject(resolvedConfig, fullpath, klass)
        }

        override fun <T: Any> bindClassAtConfigPath(configPath: String, klass: TypeReference<T>) {
            val fullpath = (path + configPath.split('.')).filter { it.isNotBlank() }.map { it.removePrefix(".").removeSuffix(".") }.joinToString(".")
            loadAndInject(resolvedConfig, fullpath, klass)
        }

        @suppress("UNCHECKED_CAST")
        fun <T: Any> loadAndInject(config: Config, fullPath: String, klass: TypeReference<T>) {
            val configAtPath = config.getConfig(fullPath)
            val asJson = configAtPath.root().render(ConfigRenderOptions.concise().setJson(true))
            val instance: T = mapper.readValue(asJson, TypeFactory.defaultInstance().constructType(klass.type))!!
            scope.registrar.addSingleton(klass, instance)
        }
    }

    init {
        resolvedConfig = configFactory()
        val registrar = ScopedKonfigRegistrar(emptyList(), scope)
        registrar.registerConfigurables()
        scope.registrar.registerInjectables()
    }
}

public interface KonfigRegistrar: InjektRegistrar {
    /**
     * import a module loading it and any submodules immediately
     */
    fun importModule(atPath: String, module: KonfigModule)

    /**
     * bind a class bindings its values from a configuration path immediately
     */
    final inline fun <reified T: Any> bindClassAtConfigPath(configPath: String) {
        bindClassAtConfigPath(configPath, fullType<T>())
    }

    /**
     * bind a class bindings its values from a configuration path immediately
     */
    fun <T: Any> bindClassAtConfigPath(configPath: String, klass: TypeReference<T>)

    /**
     * bind a class bindings its values from a configuration path immediately
     */
    final inline fun <reified T: Any> bindClassAtConfigPath(configPath: String, klass: Class<T>) {
        bindClassAtConfigPath(configPath, fullType<T>())
    }

    /**
     * bind a class bindings its values from the root of the current configuration path immediately
     */
    final inline fun <reified T: Any> bindClassAtConfigRoot() {
        bindClassAtConfigRoot(fullType<T>())
    }

    /**
     * bind a class bindings its values from the root of the current configuration path immediately
     */
    fun <T: Any> bindClassAtConfigRoot(klass: TypeReference<T>)

    /**
     * bind a class bindings its values from the root of the current configuration path immediately
     */
    final inline fun <reified T: Any> bindClassAtConfigRoot(klass: Class<T>) {
        bindClassAtConfigRoot(fullType<T>())
    }
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

