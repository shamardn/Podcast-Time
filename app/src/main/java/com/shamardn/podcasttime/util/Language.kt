package com.shamardn.podcasttime.util


enum class Language(val languageCode: String) {
    ENGLISH("en"),
    ARABIC("ar");

    companion object {
        fun fromLanguageCode(languageCode: String): Language {
            return values().associateBy(Language::languageCode)[languageCode] ?: throw IllegalArgumentException("No enum constant!")
        }
    }
}