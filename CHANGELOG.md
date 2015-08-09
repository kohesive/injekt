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