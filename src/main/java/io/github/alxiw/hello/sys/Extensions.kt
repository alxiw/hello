package io.github.alxiw.hello.sys

fun Response.isOk(): Boolean {
    return this == Response.SUCCESS
}
