package com.devijulias

import io.ktor.server.engine.*
import io.ktor.server.cio.*
import com.devijulias.plugins.*
import com.devijulias.services.WebcamService


fun main() {
    val service = WebcamService.getService()
    service.start()
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureRouting()
    }.start(wait = true)
}
