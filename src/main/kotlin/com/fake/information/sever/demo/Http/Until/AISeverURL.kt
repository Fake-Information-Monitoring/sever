package com.fake.information.sever.demo.Http.Until

enum class AISeverURL(var url: String) {
    RUMOR_URL("http://127.0.0.1:4336/VerifyRumor") {
        override fun toString(): String {
            return this.url
        }
    },
    SENSITIVE_WORD_URL("http://127.0.0.1:4336/VerifyOtherWords") {
        override fun toString(): String {
            return this.url
        }
    },
    ZOMBIES_URL("http://127.0.0.1:4336/VerifyZombies") {
        override fun toString(): String {
            return this.url
        }
    }
}