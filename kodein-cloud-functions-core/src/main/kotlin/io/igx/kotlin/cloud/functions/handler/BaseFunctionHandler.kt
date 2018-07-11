/*
 *  Copyright 2018 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */


package io.igx.kotlin.cloud.functions.handler

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.igx.kotlin.cloud.functions.ModuleLoader
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.allInstances
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import java.lang.reflect.Type
import java.util.*

/**
 * @author Vinicius Carvalho
 */
open class BaseFunctionHandler {

    protected val kodein = Kodein {
        loadModules().forEach { module -> import(module) }
        bind<ObjectMapper>() with singleton { ObjectMapper().registerKotlinModule() }
    }

    private  fun loadModules(): List<Kodein.Module> {
        var list = mutableListOf<Kodein.Module>()
        val loader = ServiceLoader.load(ModuleLoader::class.java)
        loader.forEach { moduleLoader -> list.addAll(moduleLoader.getModules()) }
        return list
    }

    protected fun loadFunction(name: String) : Any {
        return kodein.direct.allInstances<Any>(tag = name)[0]
    }

    protected fun inputType(fn: Any) : Type? {
        var type: Type? = null
        return type
    }

    protected fun outputType(fn: Any) : Type? {
        var type: Type? = null
        return type
    }
}