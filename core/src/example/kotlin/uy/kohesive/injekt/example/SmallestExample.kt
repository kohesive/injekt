package uy.kohesive.injekt.example

import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.InjektMain
import uy.kohesive.injekt.InjektRegistrar
import java.text.SimpleDateFormat
import java.util.*
import kotlin.platform.platformStatic


class SmallApp {
    companion object : InjektMain() { // my app starts here with a static main()
        platformStatic public fun main(args: Array<String>) {
            SmallApp().run()
        }

        override fun InjektRegistrar.registerInjektables() {
            addPerThreadFactory { SimpleDateFormat("yyyy-MM-dd") }
        }
    }

    public fun run(dateFormatter: SimpleDateFormat = Injekt.get()) {
        System.out.println("I survived the date formatting!!! ${dateFormatter.format(Date())}")
    }
}