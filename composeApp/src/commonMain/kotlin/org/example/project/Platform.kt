package org.example.project

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun readFile(path: String): String