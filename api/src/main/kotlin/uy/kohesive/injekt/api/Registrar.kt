package uy.kohesive.injekt.api

interface InjektRegistrar: InjektRegistry, InjektFactory  {
    public fun importModule(submodule: InjektModule) {
        submodule.registerWith(this)
    }
}
