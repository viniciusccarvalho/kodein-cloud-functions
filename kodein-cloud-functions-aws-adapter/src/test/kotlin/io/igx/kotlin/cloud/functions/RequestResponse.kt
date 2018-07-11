package io.igx.functions

import io.igx.kotlin.cloud.functions.Request
import io.igx.kotlin.cloud.functions.Response


class RequestResponse : (Request) -> (Response){
    override fun invoke(request: Request): Response {
        return Response(200)
    }
}