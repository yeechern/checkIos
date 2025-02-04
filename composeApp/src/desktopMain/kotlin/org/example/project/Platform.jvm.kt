package org.example.project

import java.nio.file.Files
import java.nio.file.Paths

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun readFile(path: String): String {
    return Files.readString(Paths.get(path))
}