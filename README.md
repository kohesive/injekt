[![CircleCI branch](https://img.shields.io/circleci/project/kohesive/injekt/master.svg)](https://circleci.com/gh/kohesive/injekt/tree/master) [![Maven Version](https://img.shields.io/maven-central/v/uy.kohesive.injekt/injekt-core.svg)](#) [![DUB](https://img.shields.io/dub/l/vibe-d.svg)](https://github.com/kohesive/injekt/blob/master/LICENSE)

# Injekt

Injekt is a crazyily easy **Dependency Injection** for Kotlin.  Although you can probably use it in other JVM languages if you are feeling lucky.

Injekt is NOT inversion of control.  It is NOT some mystical class file manipulator.  It is NOT complex.  But it IS powerful.

## Maven Dependnecy

First, include the dependency in your Gradle / Maven projects, ones that have Kotlin configured for Kotlin M12 versions `0.12.1218` or `0.12.1230`

```
compile "uy.kohesive.injekt:injekt-core:1.0.0"
```

*(deploy note:  Maven repo publishing is in progress, should appear soon)*

## Injektor Module

At the earliest point in your application startup, you register singletons, factories and your logging factories.  For the simplest version of this process, you can use the InjektModule on an object or companion object (from [Injekt Examples](https://github.com/kohesive/injekt/blob/master/core/src/example/kotlin/uy/kohesive/injekt/example/MyApp.kt))

```kotlin
class MyApp {
    companion object : InjektMain() {
        // my app starts here with a static main()
        platformStatic public fun main(args: Array<String>) {
            MyApp().run()
        }

        // the InjektModule() will call me back here on a method I override.  And all my functions for registration are
        // easy to find on the receiver class
        override fun InjektRegistrar.registerInjektables() {
            // let's setup my logger first
            addLoggerFactory<Logger>({ byName -> LoggerFactory.getLogger(byName) }, { byClass -> LoggerFactory.getLogger(byClass) })

            // now some singletons
            addSingleton(HttpServerConfig("0.0.0.0", 8080, 16))
            addSingleton(DatabaseConnectionConfig("db.myhost.com", "sa", "sillycat"))

            // or a lazy singleton factory
            addSingletonFactory { DontCreateUntilWeNeedYa() }

            // or lets only have one database connection per thread, basically a singleton per thread
            addPerThreadFactory { JdbcDatabaseConnection(Injekt.get()) }  // wow, nested injektions!!!

            // or give me a new one each time it is injekted
            addFactory { LazyDazy() }

            // or be weird and use extension functions on classes that are visible while in this lambda
            KnownObject().registerAsSingleton()
            KnownObject::class.registerFactory { KnownObject() }

            // and we also have factories that use a key (or single parameter) to return an instance
            val pets = listOf(NamedPet("Bongo"), NamedPet("Dancer"), NamedPet("Cheetah")).map { it.name to it}.toMap()
            addPerKeyFactory { petName: String -> pets.get(petName) }

            // use prebuilt Injektable packages
            importModule(AmazonS3InjektModule)
        }
    }

    ...
}
```

And once they are registered, anything else in the system can access them, for example as class properties they can be injekted using delegates:

```kotlin
    val log: Logger by Delegates.injektLogger()
    val laziest: LazyDazy by Delegates.injektLazy()
    val lessLazy: LazyDazy by Delegates.injektValue()
```

or directly as assignments both as property declarations and local assignemtns:

```kotlin
    val notLazy1: LazyDazy = Injekt.get()
    val notLazy2 = Injekt.get<LazyDazy>()
```

And they can be used in constructors and methods as default parameters:

```kotlin
    public fun foo(dbConnectParms: DatabaseConfig = Injekt.get()) { ... }
```

And since we have registered in the first example a mix of types, including thread specific injektions and key/parameter based, here they are in action:

```kotlin
 public fun run() {
        // even local variables can be injekted, or rather "got"
        val something = Injekt.get<DontCreateUntilWeNeedYa>()
        startHttpServer()
    }

    // and we can inject into methods by using Kotlin default parameters
    public fun startHttpServer(httpCfg: HttpServerConfig = Injekt.get()) {
        log.debug("HTTP Server starting on ${httpCfg.host}:${httpCfg.port}")
        HttpServer(httpCfg.host, httpCfg.port).withThreads(httpCfg.workerThreads).handleRequest { context ->
            val db: JdbcDatabaseConnection = Injekt.get()    // we have a connection per thread now!

            if (context.params.containsKey("pet")) {
                // injekt from a factory that requires a key / parameter
                val pet: NamedPet = Injekt.get(context.params.get("pet")!!)
                // or other form without reified parameters
                val pet2 = Injekt.get<NamedPet>(context.params.get("pet")!!)
            }
        }
    }
```

## Packaged Injektables

Now that you have mastered Injektions, let's make modules of our application provide their own injektables.  Say our Amazon AWS helper module has a properly configured credential provider chain, and can make clients for us nicely.  It is best to have that module decide the construction and make it available to other modules.  And it's easy.  Create an object that extends `InjektModule` and then it is pretty much the same as before:

```kotlin
public object AmazonS3InjektModule : InjektModule {
    override fun InjektRegistrar.exportInjektables() {
        addSingletonFactory { AmazonS3Client(defaultCredentialsProviderChain()) }
    }
}
```

The only difference between an `InjektMain` and `InjektModule` object is that a `InjektMain` is automatically called to initalize, whereas an `InjektModule` does not do anything until it is imported by another `InjektMain` or `InjektModule`.  Using `InjektModule` is simple, go back to the first example at the top of this page and you will see it imported with a simple

```kotlin
 // use prebuilt Injektable packages
importModule(AmazonS3InjektModule)
```

Note:  if you extend `InjektMain` you can also implement `InjektModule` interface and be both at the same time.  When doing this, put common Injektables into the module `exportInjektables()` method and import it during the main `registerInjektables()` method with simple:

```kotlin
importInjektables(this)
```

## Coming soon... (RoadMap)

* Konfiguration loading, binding and injektion as a separate module.
* Tell me what you would like to see, add Issues here in Github with requests.




