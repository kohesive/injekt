package uy.kohesive.injekt

interface InjektRegistrar: InjektRegistry, InjektFactory  {
    public fun importModule(submodule: InjektModule) {
        submodule.registerWith(this)
    }
}
