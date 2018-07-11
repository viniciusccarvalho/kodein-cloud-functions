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

package io.igx.kotlin.cloud.functions.aws

import com.amazonaws.services.lambda.runtime.Context
import com.amazonaws.services.lambda.runtime.RequestStreamHandler
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import io.igx.kotlin.cloud.functions.ModuleLoader
import org.kodein.di.Kodein
import org.kodein.di.direct
import org.kodein.di.generic.allInstances
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import java.io.InputStream
import java.io.OutputStream
import java.lang.IllegalStateException
import java.lang.reflect.Modifier
import java.lang.reflect.Type
import java.util.*
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.jvm.javaType

class KodeinStreamHandler : RequestStreamHandler {

    val kodein = Kodein {
        loadModules().forEach { module -> import(module) }
        bind<ObjectMapper>() with singleton { ObjectMapper().registerKotlinModule() }
    }

    override fun handleRequest(input: InputStream?, output: OutputStream?, context: Context?) {

        val mapper: ObjectMapper by kodein.instance<ObjectMapper>()

        val function = System.getenv("FUNCTION_NAME")

        if (function == null || function.isEmpty()) {
            throw IllegalStateException("Can not locate function in catalog, please make sure you provide a FUNCTION_NAME environment variable")
        }

        val fn = kodein.direct.allInstances<Any>(tag = function)[0]

        var type: Type? = null
        //Ok this is a hack to support high order functions, which type can't be determined. We use java reflection instead,
        //the inner class generated always have a final method with the right signature
        try{
            type = fn::class.declaredFunctions.first().parameters[1].type.javaType
        } catch(e: UnsupportedOperationException){
            fn::class.java.declaredMethods.forEach { method ->
                if(Modifier.isFinal(method.modifiers)){
                    type = method.parameters[0].type
                }
             }
        }

        val arg = mapper.readValue<Any>(input, mapper.typeFactory.constructType(type))

        val fx = fn as Function1<Any, *>

        mapper.writeValue(output, fx(arg))

    }

    fun loadModules(): List<Kodein.Module> {
        var list = mutableListOf<Kodein.Module>()
        val loader = ServiceLoader.load(ModuleLoader::class.java)
        loader.forEach { moduleLoader -> list.addAll(moduleLoader.getModules()) }
        return list
    }

}