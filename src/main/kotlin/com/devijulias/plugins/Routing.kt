package com.devijulias.plugins

import com.devijulias.services.WebcamService
import io.ktor.server.routing.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.request.*
import org.bytedeco.opencv.opencv_core.*
import org.bytedeco.opencv.opencv_imgproc.*
import org.bytedeco.opencv.global.opencv_core.*
import org.bytedeco.opencv.global.opencv_imgproc.*
import org.bytedeco.opencv.global.opencv_imgcodecs.*
import io.ktor.server.http.content.*
import io.ktor.utils.io.core.*
import java.io.*
import io.ktor.utils.io.streams.*
import kotlinx.coroutines.delay
import kotlin.text.toByteArray

fun Application.configureRouting() {

    routing {
        get("/") {
            val service = WebcamService.getService()
            call.response.headers.append("Connection", "close")
            call.response.cacheControl(CacheControl.NoCache(CacheControl.Visibility.Public))
            call.respondOutputStream(ContentType("multipart", "x-mixed-replace",
                listOf(HeaderValueParam("boundary", "boundarydonotcross", false))),
                HttpStatusCode.OK
            ) {
                while (true) {
                    val frame = service.frame
                    if (frame.empty()) {
                        delay(50)
                        continue
                    }
                    val jpgImg = ByteArray(frame.total().toInt())
                    imencode(".jpg", frame, jpgImg)
                    this.writePacket {
                        this.writeFully("--boundarydonotcross\r\n".toByteArray())
                        this.writeFully("Content-Type: image/jpeg\r\n".toByteArray())
                        this.writeFully(("Content-Length: " + jpgImg.size.toString() + "\r\n\r\n").toByteArray())
                        this.writeFully(jpgImg)
                        this.writeFully("\r\n".toByteArray())
                    }
                }
            }
        }
    }
}
