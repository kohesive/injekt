package uy.kohesive.injekt.api

@Deprecated(replaceWith = ReplaceWith("InjektScope", "uy.kohesive.injekt.api.InjektScope"), level = DeprecationLevel.WARNING, message = "When used with modules, replace InjektRegistry with InjektScope for extension functions such as InjektRegistry.registerInjectables()")
interface InjektRegistrar: InjektRegistry, InjektFactory  {}
