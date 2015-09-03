package uy.kohesive.injekt.example

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import uy.kohesive.injekt.*
import uy.kohesive.injekt.api.*
import kotlin.platform.platformStatic
import kotlin.properties.Delegates

class MyApp {
    companion object : InjektMain() {
        // my app starts here with a static main()
        platformStatic public fun main(args: Array<String>) {
            MyApp().run()
        }

        // the InjektModule() will call me back here on a method I override.  And all my functions for registration are
        // easy to find on the receiver class
        override fun InjektRegistrar.registerInjectables() {
            // let's setup my logger first
            addLoggerFactory({ byName -> LoggerFactory.getLogger(byName) }, { byClass -> LoggerFactory.getLogger(byClass) })

            // now some singletons
            addSingleton(HttpServerConfig("0.0.0.0", 8080, 16))
            addSingleton(DatabaseConnectionConfig("db.myhost.com", "sa", "sillycat"))

            // or a lazy singleton factory
            addSingletonFactory { DontCreateUntilWeNeedYa() }

            // or lets only have one database connection per thread, basically a singleton per thread
            addPerThreadFactory { JdbcDatabaseConnection(Injekt.get()) }  // wow, nested inections!!!

            // or give me a new one each time it is injected
            addFactory { LazyDazy() }

            // or use extension functions on classes that are visible while in this lambda
            KnownObject().registerAsSingleton()

            // and we also have factories that use a key (or single parameter) to return an instance
            val pets = listOf(NamedPet("Bongo"), NamedPet("Dancer"), NamedPet("Cheetah")).map { it.name to it}.toMap()
            addPerKeyFactory { petName: String -> pets.get(petName)!! }

            // use prebuilt injectable packages
            importModule(AmazonS3InjektModule)
        }
    }

    // === Ok, now let's use these factories ===

    // injections to properties can be delegates, we've added a bunch of extensions for inject now, lazy, and more
    val log: Logger by Delegates.injectLogger()
    val laziest: LazyDazy by Delegates.injectLazy()
    val lessLazy: LazyDazy by Delegates.injectValue()

    public fun run() {
        // even local variables can be injected, or rather "got"
        val something = Injekt.get<DontCreateUntilWeNeedYa>()
        startHttpServer()
    }

    // and we can inject into methods by using Kotlin default parameters
    public fun startHttpServer(httpCfg: HttpServerConfig = Injekt.get()) {
        log.debug("HTTP Server starting on ${httpCfg.host}:${httpCfg.port}")
        HttpServer(httpCfg.host, httpCfg.port).withThreads(httpCfg.workerThreads).handleRequest { context ->
            val db: JdbcDatabaseConnection = Injekt.get()    // we have a connection per thread now!

            if (context.params.containsKey("pet")) {
                // inject from a factory that requires a key / parameter
                val pet: NamedPet = Injekt.get(context.params.get("pet")!!)
                // or other form without reified parameters
                val pet2 = Injekt.get<NamedPet>(context.params.get("pet")!!)
            }
        }
    }
}
