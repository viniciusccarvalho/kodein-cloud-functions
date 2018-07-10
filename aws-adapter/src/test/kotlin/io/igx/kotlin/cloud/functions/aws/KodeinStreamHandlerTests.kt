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

import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.EnvironmentVariables
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

class KodeinStreamHandlerTests {
    @get:Rule
    val environmentVariables = EnvironmentVariables()

    @Test
    fun testSampleClas() {
        val handler = KodeinStreamHandler()
        val inputStream = ByteArrayInputStream("{\"name\": \"foo\", \"value\": \"bar\"}".toByteArray())
        environmentVariables.set("FUNCTION_NAME", "fooBar")
        handler.handleRequest(inputStream, ByteArrayOutputStream(), null)
    }

    @Test(expected = IllegalStateException::class)
    fun testMissingFunctionName() {
        val handler = KodeinStreamHandler()
        val inputStream = ByteArrayInputStream("{\"name\": \"foo\", \"value\": \"bar\"}".toByteArray())
        handler.handleRequest(inputStream, ByteArrayOutputStream(), null)
    }

    @Test
    fun testHighOrderFunction() {
        val handler = KodeinStreamHandler()
        environmentVariables.set("FUNCTION_NAME", "reqRes")
        val inputStream = ByteArrayInputStream("{\"type\": \"foo\"}".toByteArray())
        handler.handleRequest(inputStream, ByteArrayOutputStream(), null)
    }
}