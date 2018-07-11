package io.igx.kotlin.cloud.functions

import io.igx.functions.FooBar
import io.igx.kotlin.cloud.functions.ModuleLoader
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton

class SampleModuleLoader : ModuleLoader {
    override fun getModules(): List<Kodein.Module> {
        val module = Kodein.Module("samples") {
            bind<Function1<Foo, Bar>>(tag = "fooBar") with singleton { FooBar() }
            bind(tag ="reqRes") from singleton { {r:Request -> Response(200)} }
        }
        return listOf(module)
    }
}