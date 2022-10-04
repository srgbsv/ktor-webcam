package com.devijulias.services

import org.bytedeco.javacv.FFmpegFrameRecorder
import org.bytedeco.opencv.opencv_core.Mat
import org.bytedeco.opencv.opencv_core.Size
import org.bytedeco.opencv.opencv_videoio.VideoCapture
import org.bytedeco.opencv.opencv_videoio.VideoWriter
import kotlin.concurrent.thread
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;


class WebcamService(val directoryName: String = "log") {
    var frame: Mat = Mat()
    var isRun: Boolean = true
    var recording: Boolean = false
    var logDirectory: String = "/home/sergey/log"
    val videoWriter: VideoWriter = VideoWriter()
    val width = 640
    val height = 480

    init {
        if (recording) {
            val instantNow = LocalDateTime.now()
            val formatted = DateTimeFormatter.ofPattern("DDMMMuuuu_HMS")
            val dateStr = formatted.format(instantNow)
            val fourcc = VideoWriter.fourcc('M'.code.toByte(), 'J'.code.toByte(), 'P'.code.toByte(), 'G'.code.toByte());
            videoWriter.open(logDirectory + "/" + dateStr + ".avi", fourcc, 25.0, Size(width, height))
        }
    }

    companion object {
        var instance: WebcamService? = null

        fun getService(): WebcamService {
            if (instance == null) {
                instance = WebcamService()
            }

            return instance as WebcamService
        }
    }

    fun start() {
        isRun = true
        println("Starting thread")
        thread(start = true) {
            val cap  = VideoCapture(2)
            while(isRun) {
                cap.read(frame)
                if (recording && videoWriter.isOpened) {
                    videoWriter.write(frame)
                }
                Thread.sleep(10)
            }
            if (videoWriter.isOpened) {
                videoWriter.close()
            }
            cap.release()
            println("Thread was stopped")
        }
    }

    fun stop() {
        println("Stopping thread")
        isRun = false
    }
}