package uy.kohesive.injekt.api

interface InjektRegistrar: InjektRegistry, InjektFactory  {
    fun importModule(submodule: InjektModule) {
        submodule.registerWith(this)
    }
}
