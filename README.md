[![Kotlin](https://img.shields.io/badge/kotlin-1.0.0--beta--1038-blue.svg)](http://kotlinlang.org) [![Maven Version](https://img.shields.io/maven-central/v/uy.kohesive.injekt/injekt-core.svg)](http://search.maven.org/#search%7Cga%7C1%7Cg%3A%22uy.kohesive.injekt%22) [![CircleCI branch](https://img.shields.io/circleci/project/kohesive/injekt/master.svg)](https://circleci.com/gh/kohesive/injekt/tree/master) [![Issues](https://img.shields.io/github/issues/kohesive/injekt.svg)](https://github.com/kohesive/injekt/issues?q=is%3Aopen) [![DUB](https://img.shields.io/dub/l/vibe-d.svg)](https://github.com/kohesive/injekt/blob/master/LICENSE) [![Kotlin Slack](https://img.shields.io/badge/chat-kotlin%20slack-orange.svg)](http://kotlinslackin.herokuapp.com)

# Injekt 

Injekt is a crazyily easy **Dependency Injection** for Kotlin.  Although you can probably use it in other JVM languages if you are feeling lucky.

Injekt is NOT inversion of control.  It is NOT some mystical class file manipulator.  It is NOT complex.  But it IS powerful.

Injekt can also load, bind to objects, and inject configuration using Typesafe Config.  Read more in the [injekt-config-typesafe module](https://github.com/klutter/klutter/tree/master/config-typesafe).

## Maven Dependnecy

Include the dependency in your Gradle / Maven projects, ones that have Kotlin configured for Kotlin 1.0.0 (BETA)

**Gradle:**
```
compile "uy.kohesive.injekt:injekt-core:1.8.+"
```

**Maven:**
```
<dependency>
    <groupId>uy.kohesive.injekt</groupId>
    <artifactId>injekt-core</artifactId>
    <version>[1.8.0,1.9.0)</version>
</dependency>
```

## Injekt "Main"

At the earliest point in your application startup, you register singletons, factories and your logging factories.  For the simplest version of this process, you can use the `InjektModule` on an object or companion object (from [Injekt Examples](https://github.com/kohesive/injekt/blob/master/core/src/example/kotlin/uy/kohesive/injekt/example/MyApp.kt))

```kotlin
class MyApp {
    companion object : InjektMain() {
        // my app starts here with a static main()
        @JvmStatic public fun main(args: Array<String>) {
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

    ...
}
```

And once they are registered, anything else in the system can access them, for example as class properties they can be injected using delegates:

```kotlin
    val log: Logger by injectLogger()
    val laziest: LazyDazy by injectLazy()
    val lessLazy: LazyDazy by injectValue()
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

And since we have registered in the first example a mix of types, including thread specific injections and key/parameter based, here they are in action:

```kotlin
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
```

## Packaged Injektables

Now that you have mastered injections, let's make modules of our application provide their own injectable items.  Say our Amazon AWS helper module has a properly configured credential provider chain, and can make clients for us nicely.  It is best to have that module decide the construction and make it available to other modules.  And it's easy.  Create an object that extends `InjektModule` and then it is pretty much the same as before:

```kotlin
public object AmazonS3InjektModule : InjektModule {
    override fun InjektRegistrar.registerInjectables() {
        addSingletonFactory { AmazonS3Client(defaultCredentialsProviderChain()) }
    }
}
```

The only difference between an `InjektMain` and `InjektModule` object is that a `InjektMain` is automatically called to initialize, whereas an `InjektModule` does not do anything until it is imported by another `InjektMain` or `InjektModule`.  Using `InjektModule` is simple, go back to the first example at the top of this page and you will see it imported with a simple

```kotlin
 // use prebuilt package
importModule(AmazonS3InjektModule)
```

Note:  if you extend `InjektMain` you are also a module that can be imported.  But beware that if you use scopes (see `InjektScope` and `InjektScopeMain` then you should not use `InjektMain` which will always import into the global scope).  More on scopes in a future release, not intended to be used yet.

## One Instance Per-Thread Factories -- a tip

When using a factory that is per-thread, be sure not to pass the object to other threads if you really intend for them to be isolated.  Lookup of such objects is from ThreadLocal storage and fast, so it is better to keep these objects for shorter durations or in situations guaranteed to stay on the same thread as that which retrieved the object.

## Injecting Configuration with Typesafe Config

Injekt + Klutter library can also load, bind to objects, and inject configuration using Typesafe Config.  Read more in the [klutter/config-typesafe](https://github.com/klutter/klutter/tree/master/config-typesafe) module.

## Generics, Erased Type and Injection

It is best to use type inference for all methods in Injekt to let the full type information come through to the system.  If the compiler can determine
the types, it is likely the full generic information is captured.  But if a value passed to Injekt does not have that information available at the calling
site, then you can specify the full generic type as a type parameter to the method, for example `Injekt.get<MyFullType<WithGenerics<AndMore>>>()`.  Methods also
accept a `TypeReference<T>` which can be passed around as a means to parameterize and send type information through the system.  The helper method `fullType<T>()`
can create a `TypeReference` for you, for example `Injekt.get(fullType<MyFullType<WithGenerics>>())`.

It is preferable that you use (in priority order):

* The inferred forms of Injekt functions that infer their type from the surrounding expression.  Do not provide a type unless something breaks.
* Or if you want to change the type recognized by Injekt, use an explicit generic type for the function `someFunction<MyType>()`.
* If those are not to your liking, then use the `TypeReference` version passing in a `typeRef<T>()` generated `TypeReference` ... this is your fallback in cases where things need full and exact control.

By doing this, you prevent surprises because you are in full control and it is obvious what types are expected.

## Coming soon... (RoadMap)

* More about scopes
* Materializing object graphs without explicit calls to Injekt
* Tell me what you would like to see, add Issues here in Github with requests.

## Recommended libraries:

Other libraries that we recommend a building blocks for Kotlin applications:

* [Kovenant](http://kovenant.komponents.nl) - promises for Kotlin, easy, fun, and async! (JVM / Android)
* [Klutter](http://github.com/klutter) - simple, small, useful things for Kotlin

## With help from...

[![Collokia Logo](https://www.collokia.com/images/collokia-logo-210x75.png)](https://www.collokia.com)




