package com.hassan.kooged

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform