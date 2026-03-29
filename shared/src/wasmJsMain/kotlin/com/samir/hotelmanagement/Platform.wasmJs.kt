package com.samir.hotelmanagement

class WasmJsPlatform : Platform {
    override val name: String = "wasmJs"
}

actual fun getPlatform(): Platform = WasmJsPlatform()
