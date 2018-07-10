package io.igx.kotlin.cloud.functions

data class Foo(val name: String, val value: String)

data class Bar(val name: String, val result: String)

data class Request(val type: String)
data class Response(val code: Int)