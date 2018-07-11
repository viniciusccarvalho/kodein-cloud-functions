package io.igx.functions

import io.igx.kotlin.cloud.functions.Bar
import io.igx.kotlin.cloud.functions.Foo


class FooBar: (Foo) -> Bar {
    override fun invoke(f: Foo): Bar {
        return Bar(f.name, f.value)
    }
}