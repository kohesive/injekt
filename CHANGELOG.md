2016-02-15 v1.14.0

* Kotlin 1.0.0

===

2016-02-10 v1.13.0-RC-1050 (only to binTray)

* Added scoped factory methods to `InjektScope` to help with Github #32
* Added `LocalScoped` class to help in using a local scope delegates
* POSSIBLE BREAKING CHANGE: Moved `InjektScope` extensions into `InjektScope` so that additional imports are not needed

===

2016-02-09 v1.12.0-RC-1050 (only to binTray)

* Upgraded to Kotlin 1.0.0-rc-1050

===

2016-02-04 v1.12.0-RC-1036

* Upgraded to Kotlin 1.0.0-rc-1036

====

2016-02-02 v1.12.0-RC-1025

* Upgraded to Kotlin 1.0.0-rc-1025

====

2016-01-20 v1.11

* Upgraded to Kotlin 1.0.0-beta-4589

====

2015-12-30 v1.10.1

* Upgraded to Kotlin 1.0.0-beta-4584

====

2016-01-20 v1.11

* Upgraded to Kotlin 1.0.0-beta-4589

====

2015-12-30 v1.10.1

* Upgraded to Kotlin 1.0.0-beta-4584

====

2015-12-22 v1.10.0

* Upgraded to Kotlin 1.0.0-beta-4583

====

2015-12-08 v1.9.0 (and version 1.8.4 is same release)

* Upgraded to Kotlin 1.0.0-beta-3595

====

2015-11-16 v1.8.3

* Upgraded to Kotlin 1.0.0-beta-2423

====

2015-11-16 v1.8.2

* Upgraded to Kotlin 1.0.0-beta-2422

====

2015-11-06 v1.8.1

* Upgraded to Kotlin 1.0.0-beta-1103

====

2015-10-06 v1.8.0

* Upgraded to Kotlin 1.0.0 ABI  (1.0.0-beta-1038)
* added getOrNull varations of methods

====

2015-10-06 v1.7.1

* Upgraded to Kotlin 0.14.451

====

2015-10-01 v1.7.0

* Upgraded to Kotlin M14 release

====

2015-09-15 v1.6.0

* deprecated methods dropped, stay on 1.5.0 if you don't want to make code changes
* BREAKING CHANGE:  some functions moved out of interface and into extension methods due to upcoming changes of dropping "final" and inline-able default methods in interfaces.  Probably a bad design choice to use them anyway, but you'll need separate imports or import * to catch all the methods now.  Since they are inline functions (and/or because moved to extensions), you MUST recompile against the new version of the library.
* Added invoke() extension to factory, so you can now also inject using `val something: MyClass = Injekt()`
* Be careful, calling Injekt["key"] or any of the inline `get(...)` functions with brackets, will crash in M13 https://youtrack.jetbrains.com/issue/KT-9211

If you have odd compiler issues, check your imports for the extension functions.  Or set Injekt API to always import * in your IDE settings.  `import uy.kohesive.injekt.api.*`

====

2015-09-15 v1.5.0

* Moved Configuration injection to Klutter, it was too much of a circular dependency and belongs there with the rest of the typesafe configuration code.  See https://github.com/klutter/klutter config-typesafe modules.
* Kotlin M13 support
* Added injekt methods with allowed default values and lambdas, so that you can injekt if present in the registry, if not use a provided value.
* BREAKING CHANGE:  dropped keyClass parameter from adding factories by key (not needed)
* Deprecated everything that accepts a Java Class parameter is deprecated, because even though we can use tricks to pick up the reified generics and circumvent type erasure, we cannot do it in every path you might create a Java CLass reference, especially since `javaClass<T>()` is deprecated and people will switch to `MyClass::class.java` which loses all generic type info.  Instead use type inference if the variable type is clear to the compiler, otherwise use the type parameters to the methods or the `fullType<MyClass>()` function to generate a fully non erased generic type structure.
* Deprecated all extensions on Delegates class, they are now top level functions to match Kotlin delegate lazy<T>()

====

2015-09-03 v1.4.0

I tried to do this without many breaking changes, but there is a chance something breaks so recompile and find out.  Deprecated methods will be removed very soon, so move away from them quickly.

* Support for generic types for injection (#13), i.e. `Something<Map<String, List<String>>`` can have a specific singleton or factory.
* Added methods accepting `TypeReference` objects for more control over generics, although the default for all methods is full generic information unless subverted by the caller (passing in a Type that has generics erased and which we cannot reify to get the generics anyway)
* added `typeRef<T>()` method which is similar to `javaClass<T>()` but contains full generic information.
* Changed upper bounds of registry types to `T:Any` to prepare for M13 which likes this better
* Registering a per key factory no longer expects the type of the key, deprecated version of method that expects that
* BREAKING CHANGE: Had to replace `aliases` method with `addAlias` one at a time to be able to reify the generics for each alias
* BREAKING CHANGE: Dropped `KClass` extension methods because they erase types and not sure if those or KType is better with coming M13

====

2015-08-24 v1.3.2

* a KonfigRegistrar is not also an InjektRegistrar so you can retrieve values if immediately bound from configuration
* bind* methods now load configuration objects immediately

====

2015-08-24 v1.3.1

* added protected resolvedConfig member to KonfigAndInjektMain in case a descendant wants the raw config object
* updated to Klutter 0.2.2

====

2015-08-21 v1.3.0

* Added Configuration injection from Typesafe Config (see [README in config-typesafe](config-typesafe/))
* Added storage in Injekt scope for addons to work within a scope

====

2015-08-11 v1.2.0

* [BREAKING CHANGE] Fixing #8 - Moved api classes to `uy.kohesive.injekt.api` package so that separate module jars do not have classes in the same package, breaking use in Android
* [BREAKING CHANGE] Remove deprecated "injekt*" delegates (replaced with "inject*")

====

2015-08-11 v1.1.1

* Fix for #7 - factories called again when value already existed, even though correct value was returned.  Fixed, although in JDK 7 concurrentHashMap doesn't have a way to prevent some chance of a second factory call, although the correct value would still be returned and the additional factory call would be tossed away.

=====

2015-08-10 v1.1.0

Seperate API from core JAR.  Core depends on API. a lot of small changes to structure for having independent scopes for injection, and cleaning.
Sorry for the breaks, but they will be tiny little changes to clean up.

API Changes:
* new property delegates that require scope to be passed in.

Core Changes:
* delegates by default point a the top level scope unless you use the scope-specific version.
* changed Injekt from object singleton that contained implementation to an var of type `InjektScope` (no API change, code should compile as-is but does need recompile)
* [BREAKING CHANGE] changed default registry/factory package to `uy.kohesive.injekt.registry.default` although unlikely referenced from user code.
* [BREAKING CHANGE] renamed `InjektInstanceFactory` to `InjektFactory`
* [BREAKING CHANGE] renamed `Injekt.Property` with delegates removed, use other Delegates.*
* [BREAKING CHANGE] `InkektRegistrar` is changed to only be a combination of two interfaces, `InjektRegistry` and `InjektFactory` and nothing else.
* [BREAKING CHANGE] changed `InjektMain` / `InjektScopedMain` to also be a module with same `registerInjectables` method
* [BREAKING CHANGE] changed `exportInjektables` to `registerInjectables`
* [BREAKING CHANGE] changed other words in method names that used "injekt" to "inject" to be more natural, old versions deprecated to be removed in 1 release
* Introduced `InjektScope` which allows different parts of the app to have different injection registries
* Introduced `InjektScopedMain` which is like `InjektMain` but for a specified scope instead of the global scope.



=====

2015-08-04 v1.0.0 released to Maven Central

First version:
uy.kohesive.injekt:injekt-core:1.0.0
