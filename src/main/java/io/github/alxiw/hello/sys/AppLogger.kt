package io.github.alxiw.hello.sys

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.lang.Exception

object AppLogger {

    private const val TAG = "APP"

    private val logger: Logger = LoggerFactory.getLogger(AppLogger::class.java)

    @JvmStatic
    fun i(message: String) {
        logger.info("$TAG $message")
    }

    @JvmStatic
    fun e(e: Exception) {
        logger.error("$TAG ${e.message}")
    }

    @JvmStatic
    fun e(e: Exception, message: String) {
        logger.error("$TAG $message", e)
    }
}
