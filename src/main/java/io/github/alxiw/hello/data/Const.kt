package io.github.alxiw.hello.data

internal object Const {
    const val DATA_DIR = "/data/"
    const val DB_URL: String = "jdbc:sqlite:${DATA_DIR}hello.db"
    const val JSON_URL: String = "/data.json"
    const val TEMP_FILE_EXT: String = ".mp3"
}
