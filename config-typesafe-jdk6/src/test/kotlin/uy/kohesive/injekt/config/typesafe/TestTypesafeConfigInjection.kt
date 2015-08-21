package uy.kohesive.injekt.config.typesafe

import com.typesafe.config.Config
import org.junit.Test
import uy.klutter.config.typesafe.MapAsConfig
import uy.klutter.config.typesafe.loadConfig
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.InjektModule
import uy.kohesive.injekt.api.InjektRegistrar
import kotlin.test.assertEquals

class TestTypesafeConfigInjection {
    companion object : KonfigAndInjektMain() {
        override fun configFactory(): Config {
            return loadConfig(MapAsConfig(kotlin.mapOf(
                    "http" to kotlin.mapOf("httpPort" to 8080, "workerThreads" to 16),
                    "data" to kotlin.mapOf("bucket" to "com.test.bucket", "region" to "us-east"),
                    "other" to kotlin.mapOf("name" to "frisbee"))))
        }

        override fun KonfigRegistrar.registerConfigurables() {
            bindClassAtConfigPath<HttpConfig>("http")
            bindClassAtConfigPath<DataConfig>("data")
            importModule("other", OtherModule)
        }

        override fun InjektRegistrar.registerInjectables() {
            addFactory { ConfiguredThing() }
            importModule(OtherModule)
        }

    }

    @Test public fun testConfigSingletonsExist() {
        val matchHttp = HttpConfig(8080,16)
        val matchData = DataConfig("com.test.bucket", "us-east")

        assertEquals(matchHttp, Injekt.get<HttpConfig>())
        assertEquals(matchData, Injekt.get<DataConfig>())
    }

    @Test public fun testFactoryUsingConfigWorks() {
        val matchHttp = HttpConfig(8080,16)
        val matchData = DataConfig("com.test.bucket", "us-east")

        val thing =  Injekt.get<ConfiguredThing>()
        assertEquals(matchHttp, thing.httpCfg)
        assertEquals(matchData, thing.dataCfg)
    }

    @Test public fun testWithModules() {
        val thing = Injekt.get<OtherThingWantingConfig>()
        assertEquals("frisbee", thing.cfg.name)
    }


    data class HttpConfig(val httpPort: Int, val workerThreads: Int)
    data class DataConfig(val bucket: String, val region: String)
    data class ConfiguredThing(val httpCfg: HttpConfig = Injekt.get(), val dataCfg: DataConfig = Injekt.get())
}


data class OtherConfig(val name: String)
data class OtherThingWantingConfig(val cfg: OtherConfig = Injekt.get())

public object OtherModule : KonfigModule, InjektModule {
    override fun KonfigRegistrar.registerConfigurables() {
        bindClassAtConfigRoot<OtherConfig>()
    }

    override fun InjektRegistrar.registerInjectables() {
        addFactory { OtherThingWantingConfig() }
    }
}




