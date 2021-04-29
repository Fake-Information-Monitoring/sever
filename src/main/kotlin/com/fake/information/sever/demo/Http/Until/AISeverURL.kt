package com.fake.information.sever.demo.Http.Until

enum class AISeverURL(var url: String) {
    ROOT_URL("http://127.0.0.1:4336"),
    RUMOR_URL("$ROOT_URL/VerifyRumor") {
        override fun toString(): String {
            return this.url
        }
    },
    SENSITIVE_WORD_URL("$ROOT_URL/VerifyOtherWords") {
        override fun toString(): String {
            return this.url
        }
    },
    ZOMBIES_URL("$ROOT_URL/VerifyZombies") {
        override fun toString(): String {
            return this.url
        }
    };

}